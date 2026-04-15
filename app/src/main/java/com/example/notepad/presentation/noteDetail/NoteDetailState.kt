package com.example.notepad.presentation.noteDetail

// ─── UI State ─────────────────────────────────────────────────────────────────

data class NoteDetailState(
    val noteId: Long? = null,
    val title: String = "",
    val content: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isNewNote: Boolean = true
)

// ─── UI Events ────────────────────────────────────────────────────────────────

sealed class NoteDetailEvent {
    data object NavigateBack : NoteDetailEvent()
    data class ShowSnackbar(val message: String) : NoteDetailEvent()
}

// ─── User Intents ─────────────────────────────────────────────────────────────

sealed class NoteDetailIntent {
    data class TitleChanged(val title: String) : NoteDetailIntent()
    data class ContentChanged(val content: String) : NoteDetailIntent()
    data object SaveNote : NoteDetailIntent()
    data object DeleteNote : NoteDetailIntent()
    data object NavigateBack : NoteDetailIntent()
}
