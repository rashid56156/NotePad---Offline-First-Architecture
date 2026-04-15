package com.example.notepad.presentation.noteDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.domain.model.Note
import com.example.notepad.domain.usecase.DeleteNoteUseCase
import com.example.notepad.domain.usecase.GetNoteByIdUseCase
import com.example.notepad.domain.usecase.UpsertNoteUseCase
import com.example.notepad.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val upsertNoteUseCase: UpsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: Long? = savedStateHandle.get<Long>("noteId")?.takeIf { it != -1L }

    private val _state = MutableStateFlow(NoteDetailState(noteId = noteId, isNewNote = noteId == null))
    val state: StateFlow<NoteDetailState> = _state.asStateFlow()

    private val _events = Channel<NoteDetailEvent>(Channel.BUFFERED)
    val events: Flow<NoteDetailEvent> = _events.receiveAsFlow()

    init {
        noteId?.let { loadNote(it) }
    }

    fun onIntent(intent: NoteDetailIntent) {
        when (intent) {
            is NoteDetailIntent.TitleChanged -> _state.update { it.copy(title = intent.title) }
            is NoteDetailIntent.ContentChanged -> _state.update { it.copy(content = intent.content) }
            is NoteDetailIntent.SaveNote -> saveNote()
            is NoteDetailIntent.DeleteNote -> deleteNote()
            is NoteDetailIntent.NavigateBack -> navigateBack()
        }
    }

    private fun loadNote(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getNoteByIdUseCase(id)
                .filterNotNull()
                .take(1)
                .collect { note ->
                    _state.update {
                        it.copy(
                            title = note.title,
                            content = note.content,
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun saveNote() {
        val currentState = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val note = Note(
                id = noteId ?: 0L,
                title = currentState.title,
                content = currentState.content
            )
            when (val result = upsertNoteUseCase(note)) {
                is Resource.Success -> {
                    _state.update { it.copy(isSaving = false) }
                    _events.send(NoteDetailEvent.NavigateBack)
                }
                is Resource.Error -> {
                    _state.update { it.copy(isSaving = false, error = result.message) }
                    _events.send(NoteDetailEvent.ShowSnackbar(result.message))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun deleteNote() {
        val id = noteId ?: return
        viewModelScope.launch {
            when (val result = deleteNoteUseCase(id)) {
                is Resource.Success -> _events.send(NoteDetailEvent.NavigateBack)
                is Resource.Error -> {
                    _state.update { it.copy(error = result.message) }
                    _events.send(NoteDetailEvent.ShowSnackbar(result.message))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch { _events.send(NoteDetailEvent.NavigateBack) }
    }
}
