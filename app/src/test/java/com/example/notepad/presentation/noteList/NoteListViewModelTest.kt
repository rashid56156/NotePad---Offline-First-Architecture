package com.example.notepad.presentation.noteList

import app.cash.turbine.test
import com.example.notepad.domain.model.Note
import com.example.notepad.domain.usecase.*
import com.example.notepad.util.NetworkMonitor
import com.example.notepad.util.Resource
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getAllNotesUseCase: GetAllNotesUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var syncNotesUseCase: SyncNotesUseCase
    private lateinit var fetchRemoteNotesUseCase: FetchRemoteNotesUseCase
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var viewModel: NoteListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getAllNotesUseCase = mockk()
        deleteNoteUseCase = mockk()
        syncNotesUseCase = mockk()
        fetchRemoteNotesUseCase = mockk()
        networkMonitor = mockk()

        // Default stubs
        every { getAllNotesUseCase() } returns flowOf(emptyList())
        every { networkMonitor.isOnline } returns flowOf(false)

        viewModel = NoteListViewModel(
            getAllNotesUseCase,
            deleteNoteUseCase,
            syncNotesUseCase,
            fetchRemoteNotesUseCase,
            networkMonitor
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty notes list`() = runTest {
        advanceUntilIdle()
        assertEquals(emptyList<Note>(), viewModel.state.value.notes)
    }

    @Test
    fun `notes are observed and state updated`() = runTest {
        val notes = listOf(
            Note(id = 1, title = "Test", content = "Content")
        )
        every { getAllNotesUseCase() } returns flowOf(notes)

        // Re-create ViewModel so init block picks up new stub
        viewModel = NoteListViewModel(
            getAllNotesUseCase, deleteNoteUseCase, syncNotesUseCase,
            fetchRemoteNotesUseCase, networkMonitor
        )
        advanceUntilIdle()

        assertEquals(notes, viewModel.state.value.notes)
    }

    @Test
    fun `delete note shows snackbar on success`() = runTest {
        coEvery { deleteNoteUseCase(any()) } returns Resource.Success(Unit)

        viewModel.events.test {
            viewModel.onIntent(NoteListIntent.DeleteNote(1L))
            advanceUntilIdle()

            val event = awaitItem()
            assertTrue(event is NoteListEvent.ShowSnackbar)
        }
    }

    @Test
    fun `search query filters notes`() = runTest {
        val notes = listOf(
            Note(id = 1, title = "Shopping list", content = "Milk"),
            Note(id = 2, title = "Meeting notes", content = "Q3 sync")
        )
        every { getAllNotesUseCase() } returns flowOf(notes)

        viewModel = NoteListViewModel(
            getAllNotesUseCase, deleteNoteUseCase, syncNotesUseCase,
            fetchRemoteNotesUseCase, networkMonitor
        )
        advanceUntilIdle()

        viewModel.onIntent(NoteListIntent.SearchQueryChanged("shopping"))

        val filtered = viewModel.state.value.filteredNotes
        assertEquals(1, filtered.size)
        assertEquals("Shopping list", filtered[0].title)
    }

    @Test
    fun `navigate to create note emits NavigateToDetail with null`() = runTest {
        viewModel.events.test {
            viewModel.onIntent(NoteListIntent.CreateNote)
            advanceUntilIdle()

            val event = awaitItem() as NoteListEvent.NavigateToDetail
            assertNull(event.noteId)
        }
    }
}
