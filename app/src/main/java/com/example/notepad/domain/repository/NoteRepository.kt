package com.example.notepad.domain.repository

import com.example.notepad.domain.model.Note
import com.example.notepad.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defined in the domain layer.
 * The data layer provides the implementation — domain never depends on data.
 */
interface NoteRepository {

    /** Observe all non-deleted notes as a reactive stream from local DB. */
    fun getAllNotes(): Flow<List<Note>>

    /** Observe a single note by ID. */
    fun getNoteById(id: Long): Flow<Note?>

    /** Insert or update a note locally and mark it as unsynced. */
    suspend fun upsertNote(note: Note): Resource<Unit>

    /** Soft-delete a note locally and mark it as unsynced. */
    suspend fun deleteNote(id: Long): Resource<Unit>

    /**
     * Sync unsynced local notes to the remote server.
     * Emits Loading → Success/Error.
     */
    suspend fun syncNotes(): Resource<Unit>

    /** Pull latest notes from remote and merge into local DB. */
    suspend fun fetchRemoteNotes(): Resource<Unit>
}
