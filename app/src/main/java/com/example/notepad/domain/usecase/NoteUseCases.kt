package com.example.notepad.domain.usecase

import com.example.notepad.domain.model.Note
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// ─── GetAllNotesUseCase ───────────────────────────────────────────────────────

class GetAllNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}

// ─── GetNoteByIdUseCase ───────────────────────────────────────────────────────

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(id: Long): Flow<Note?> = repository.getNoteById(id)
}

// ─── UpsertNoteUseCase ────────────────────────────────────────────────────────

class UpsertNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    /**
     * Validates the note before delegating to the repository.
     * Business rules live here, not in the ViewModel or Repository.
     */
    suspend operator fun invoke(note: Note): Resource<Unit> {
        if (note.title.isBlank() && note.content.isBlank()) {
            return Resource.Error("Note cannot be empty.")
        }
        val trimmed = note.copy(
            title = note.title.trim(),
            content = note.content.trim(),
            updatedAt = System.currentTimeMillis(),
            isSynced = false
        )
        return repository.upsertNote(trimmed)
    }
}

// ─── DeleteNoteUseCase ────────────────────────────────────────────────────────

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Resource<Unit> = repository.deleteNote(id)
}

// ─── SyncNotesUseCase ─────────────────────────────────────────────────────────

class SyncNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(): Resource<Unit> = repository.syncNotes()
}

// ─── FetchRemoteNotesUseCase ──────────────────────────────────────────────────

class FetchRemoteNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(): Resource<Unit> = repository.fetchRemoteNotes()
}
