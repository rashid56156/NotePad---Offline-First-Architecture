package com.example.notepad.presentation.noteList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.domain.usecase.*
import com.example.notepad.util.NetworkMonitor
import com.example.notepad.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val syncNotesUseCase: SyncNotesUseCase,
    private val fetchRemoteNotesUseCase: FetchRemoteNotesUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _state = MutableStateFlow(NoteListState())
    val state: StateFlow<NoteListState> = _state.asStateFlow()

    // Channel for one-time events (navigation, snackbars)
    private val _events = Channel<NoteListEvent>(Channel.BUFFERED)
    val events: Flow<NoteListEvent> = _events.receiveAsFlow()

    init {
        observeNotes()
        observeNetwork()
    }

    fun onIntent(intent: NoteListIntent) {
        when (intent) {
            is NoteListIntent.CreateNote -> navigateTo(null)
            is NoteListIntent.OpenNote -> navigateTo(intent.noteId)
            is NoteListIntent.DeleteNote -> deleteNote(intent.noteId)
            is NoteListIntent.SearchQueryChanged -> updateSearch(intent.query)
            is NoteListIntent.SyncNotes -> syncNotes()
            is NoteListIntent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun observeNotes() {
        getAllNotesUseCase()
            .onEach { notes -> _state.update { it.copy(notes = notes, isLoading = false) } }
            .catch { e -> _state.update { it.copy(error = e.localizedMessage, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    private fun observeNetwork() {
        networkMonitor.isOnline
            .onEach { online ->
                _state.update { it.copy(isOnline = online) }
                // Auto-sync when coming back online
                if (online) syncNotes()
            }
            .launchIn(viewModelScope)
    }

    private fun deleteNote(id: Long) {
        viewModelScope.launch {
            when (val result = deleteNoteUseCase(id)) {
                is Resource.Success -> _events.send(NoteListEvent.ShowSnackbar("Note deleted"))
                is Resource.Error -> _state.update { it.copy(error = result.message) }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun syncNotes() {
        viewModelScope.launch {
            _state.update { it.copy(isSyncing = true, syncMessage = null) }
            when (val result = syncNotesUseCase()) {
                is Resource.Success -> {
                    _state.update { it.copy(isSyncing = false, syncMessage = "Synced ✓") }
                    // Also pull latest from remote
                    fetchRemoteNotesUseCase()
                }
                is Resource.Error -> {
                    _state.update { it.copy(isSyncing = false, error = result.message) }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun updateSearch(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun navigateTo(noteId: Long?) {
        viewModelScope.launch {
            _events.send(NoteListEvent.NavigateToDetail(noteId))
        }
    }
}
