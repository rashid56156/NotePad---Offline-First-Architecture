package com.example.notepad.data.repository

import com.example.notepad.data.local.dao.NoteDao
import com.example.notepad.data.mapper.toDomain
import com.example.notepad.data.mapper.toEntity
import com.example.notepad.data.mapper.toUpsertRequest
import com.example.notepad.data.remote.api.NoteApi
import com.example.notepad.data.remote.dto.DeleteNoteRequest
import com.example.notepad.domain.model.Note
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao,
    private val api: NoteApi
) : NoteRepository {

    // ─── Local reads (always from Room, reactive) ──────────────────────────────

    override fun getAllNotes(): Flow<List<Note>> =
        dao.observeAllNotes().map { entities -> entities.map { it.toDomain() } }

    override fun getNoteById(id: Long): Flow<Note?> =
        dao.observeNoteById(id).map { it?.toDomain() }

    // ─── Local writes (write locally first, mark unsynced) ────────────────────

    override suspend fun upsertNote(note: Note): Resource<Unit> = runCatching {
        dao.upsert(note.toEntity())
    }.fold(
        onSuccess = { Resource.Success(Unit) },
        onFailure = { Resource.Error("Failed to save note: ${it.localizedMessage}", it) }
    )

    override suspend fun deleteNote(id: Long): Resource<Unit> = runCatching {
        dao.softDelete(id)
    }.fold(
        onSuccess = { Resource.Success(Unit) },
        onFailure = { Resource.Error("Failed to delete note: ${it.localizedMessage}", it) }
    )

    // ─── Sync: push unsynced local changes to remote ──────────────────────────

    override suspend fun syncNotes(): Resource<Unit> {
        val unsynced = runCatching { dao.getUnsyncedNotes() }.getOrElse {
            return Resource.Error("Cannot read local notes: ${it.localizedMessage}", it)
        }

        val syncedIds = mutableListOf<Long>()
        val errors = mutableListOf<String>()

        for (entity in unsynced) {
            val note = entity.toDomain()
            val result = if (note.isDeleted) {
                runCatching { api.deleteNote(DeleteNoteRequest(note.id)) }
            } else {
                runCatching { api.upsertNote(note.toUpsertRequest()) }
            }

            result.fold(
                onSuccess = { response ->
                    if (response.isSuccessful) {
                        syncedIds.add(note.id)
                    } else {
                        errors.add("Note ${note.id}: HTTP ${response.code()}")
                    }
                },
                onFailure = { errors.add("Note ${note.id}: ${it.localizedMessage}") }
            )
        }

        // Mark successfully synced notes
        if (syncedIds.isNotEmpty()) {
            dao.markAsSynced(syncedIds)
            dao.purgeDeletedSyncedNotes()
        }

        return if (errors.isEmpty()) {
            Resource.Success(Unit)
        } else {
            Resource.Error("Partial sync failure: ${errors.joinToString()}")
        }
    }

    // ─── Fetch: pull remote notes and merge into local ────────────────────────

    override suspend fun fetchRemoteNotes(): Resource<Unit> = runCatching {
        val response = api.getAllNotes()
        if (response.isSuccessful) {
            val remote = response.body() ?: emptyList()
            // Merge: remote wins for synced notes; local unsynced changes are preserved
            val entities = remote.map { it.toEntity() }
            dao.upsertAll(entities)
            Resource.Success(Unit)
        } else {
            Resource.Error("Server error: ${response.code()} ${response.message()}")
        }
    }.getOrElse {
        Resource.Error("Network error: ${it.localizedMessage}", it)
    }
}
