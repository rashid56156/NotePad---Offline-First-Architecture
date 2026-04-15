package com.example.notepad.presentation.noteList

import com.example.notepad.domain.model.Note

// ─── UI State ─────────────────────────────────────────────────────────────────

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val error: String? = null,
    val syncMessage: String? = null,
    val searchQuery: String = "",
    val isOnline: Boolean = false
) {
    val filteredNotes: List<Note>
        get() = if (searchQuery.isBlank()) notes
        else notes.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.content.contains(searchQuery, ignoreCase = true)
        }
}

// ─── UI Events (one-time side effects) ────────────────────────────────────────

sealed class NoteListEvent {
    data class NavigateToDetail(val noteId: Long?) : NoteListEvent()
    data class ShowSnackbar(val message: String) : NoteListEvent()
}

// ─── User Intents ─────────────────────────────────────────────────────────────

sealed class NoteListIntent {
    data object CreateNote : NoteListIntent()
    data class OpenNote(val noteId: Long) : NoteListIntent()
    data class DeleteNote(val noteId: Long) : NoteListIntent()
    data class SearchQueryChanged(val query: String) : NoteListIntent()
    data object SyncNotes : NoteListIntent()
    data object DismissError : NoteListIntent()
}
