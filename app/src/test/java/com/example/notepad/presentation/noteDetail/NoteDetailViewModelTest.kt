package com.example.notepad.presentation.noteDetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.notepad.domain.model.Note
import com.example.notepad.domain.usecase.DeleteNoteUseCase
import com.example.notepad.domain.usecase.GetNoteByIdUseCase
import com.example.notepad.domain.usecase.UpsertNoteUseCase
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
class NoteDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getNoteByIdUseCase: GetNoteByIdUseCase
    private lateinit var upsertNoteUseCase: UpsertNoteUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getNoteByIdUseCase = mockk()
        upsertNoteUseCase = mockk()
        deleteNoteUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(noteId: Long? = null): NoteDetailViewModel {
        val handle = SavedStateHandle(
            mapOf("noteId" to (noteId ?: -1L))
        )
        return NoteDetailViewModel(
            getNoteByIdUseCase,
            upsertNoteUseCase,
            deleteNoteUseCase,
            handle
        )
    }

    // ─── Initial state ────────────────────────────────────────────────────────

    @Test
    fun `new note — isNewNote is true and fields are empty`() = runTest {
        val vm = buildViewModel(noteId = null)
        advanceUntilIdle()

        with(vm.state.value) {
            assertTrue(isNewNote)
            assertEquals("", title)
            assertEquals("", content)
            assertFalse(isLoading)
        }
    }

    @Test
    fun `existing note — loads title and content from use case`() = runTest {
        val note = Note(id = 5L, title = "Loaded Title", content = "Loaded Content")
        every { getNoteByIdUseCase(5L) } returns flowOf(note)

        val vm = buildViewModel(noteId = 5L)
        advanceUntilIdle()

        assertEquals("Loaded Title", vm.state.value.title)
        assertEquals("Loaded Content", vm.state.value.content)
        assertFalse(vm.state.value.isNewNote)
    }

    // ─── Field updates ────────────────────────────────────────────────────────

    @Test
    fun `TitleChanged intent updates title in state`() = runTest {
        val vm = buildViewModel()
        vm.onIntent(NoteDetailIntent.TitleChanged("My Note"))
        assertEquals("My Note", vm.state.value.title)
    }

    @Test
    fun `ContentChanged intent updates content in state`() = runTest {
        val vm = buildViewModel()
        vm.onIntent(NoteDetailIntent.ContentChanged("Some content"))
        assertEquals("Some content", vm.state.value.content)
    }

    // ─── Save ─────────────────────────────────────────────────────────────────

    @Test
    fun `SaveNote on success — navigates back`() = runTest {
        coEvery { upsertNoteUseCase(any()) } returns Resource.Success(Unit)
        val vm = buildViewModel()
        vm.onIntent(NoteDetailIntent.TitleChanged("Title"))

        vm.events.test {
            vm.onIntent(NoteDetailIntent.SaveNote)
            advanceUntilIdle()

            assertEquals(NoteDetailEvent.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `SaveNote on error — shows snackbar with error message`() = runTest {
        coEvery { upsertNoteUseCase(any()) } returns Resource.Error("Note cannot be empty.")
        val vm = buildViewModel()

        vm.events.test {
            vm.onIntent(NoteDetailIntent.SaveNote)
            advanceUntilIdle()

            val event = awaitItem() as NoteDetailEvent.ShowSnackbar
            assertEquals("Note cannot be empty.", event.message)
        }
    }

    @Test
    fun `SaveNote sets isSaving true during save then false after`() = runTest {
        coEvery { upsertNoteUseCase(any()) } coAnswers {
            kotlinx.coroutines.delay(100)
            Resource.Success(Unit)
        }
        val vm = buildViewModel()
        vm.onIntent(NoteDetailIntent.TitleChanged("T"))
        vm.onIntent(NoteDetailIntent.SaveNote)

        advanceTimeBy(50)
        assertTrue(vm.state.value.isSaving)

        advanceUntilIdle()
        assertFalse(vm.state.value.isSaving)
    }

    // ─── Delete ───────────────────────────────────────────────────────────────

    @Test
    fun `DeleteNote on existing note — navigates back on success`() = runTest {
        val note = Note(id = 7L, title = "To delete", content = "Gone")
        every { getNoteByIdUseCase(7L) } returns flowOf(note)
        coEvery { deleteNoteUseCase(7L) } returns Resource.Success(Unit)

        val vm = buildViewModel(noteId = 7L)
        advanceUntilIdle()

        vm.events.test {
            vm.onIntent(NoteDetailIntent.DeleteNote)
            advanceUntilIdle()

            assertEquals(NoteDetailEvent.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `DeleteNote on new note — does nothing`() = runTest {
        val vm = buildViewModel(noteId = null)
        advanceUntilIdle()

        vm.events.test {
            vm.onIntent(NoteDetailIntent.DeleteNote)
            advanceUntilIdle()
            expectNoEvents()
        }
        coVerify(exactly = 0) { deleteNoteUseCase(any()) }
    }

    @Test
    fun `DeleteNote on error — emits ShowSnackbar`() = runTest {
        val note = Note(id = 3L, title = "T", content = "C")
        every { getNoteByIdUseCase(3L) } returns flowOf(note)
        coEvery { deleteNoteUseCase(3L) } returns Resource.Error("Failed to delete")

        val vm = buildViewModel(noteId = 3L)
        advanceUntilIdle()

        vm.events.test {
            vm.onIntent(NoteDetailIntent.DeleteNote)
            advanceUntilIdle()

            val event = awaitItem() as NoteDetailEvent.ShowSnackbar
            assertEquals("Failed to delete", event.message)
        }
    }

    // ─── Navigation ───────────────────────────────────────────────────────────

    @Test
    fun `NavigateBack intent emits NavigateBack event`() = runTest {
        val vm = buildViewModel()

        vm.events.test {
            vm.onIntent(NoteDetailIntent.NavigateBack)
            advanceUntilIdle()

            assertEquals(NoteDetailEvent.NavigateBack, awaitItem())
        }
    }
}
