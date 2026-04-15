package com.example.notepad.domain.usecase

import com.example.notepad.domain.model.Note
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.util.Resource
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

// ─── DeleteNoteUseCaseTest ────────────────────────────────────────────────────

class DeleteNoteUseCaseTest {

    private lateinit var repository: NoteRepository
    private lateinit var useCase: DeleteNoteUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteNoteUseCase(repository)
    }

    @Test
    fun `delegates delete call to repository with correct id`() = runTest {
        coEvery { repository.deleteNote(42L) } returns Resource.Success(Unit)

        val result = useCase(42L)

        assertTrue(result is Resource.Success)
        coVerify(exactly = 1) { repository.deleteNote(42L) }
    }

    @Test
    fun `propagates error from repository`() = runTest {
        coEvery { repository.deleteNote(any()) } returns Resource.Error("DB locked")

        val result = useCase(1L)

        assertTrue(result is Resource.Error)
        assertEquals("DB locked", (result as Resource.Error).message)
    }
}

// ─── GetNoteByIdUseCaseTest ───────────────────────────────────────────────────

class GetNoteByIdUseCaseTest {

    private lateinit var repository: NoteRepository
    private lateinit var useCase: GetNoteByIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetNoteByIdUseCase(repository)
    }

    @Test
    fun `returns note flow from repository`() = runTest {
        val note = Note(id = 1L, title = "Test", content = "Body")
        every { repository.getNoteById(1L) } returns flowOf(note)

        val result = useCase(1L).first()

        assertEquals(note, result)
    }

    @Test
    fun `returns null flow when note does not exist`() = runTest {
        every { repository.getNoteById(99L) } returns flowOf(null)

        val result = useCase(99L).first()

        assertNull(result)
    }
}

// ─── GetAllNotesUseCaseTest ───────────────────────────────────────────────────

class GetAllNotesUseCaseTest {

    private lateinit var repository: NoteRepository
    private lateinit var useCase: GetAllNotesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAllNotesUseCase(repository)
    }

    @Test
    fun `returns all notes from repository`() = runTest {
        val notes = listOf(
            Note(id = 1L, title = "A", content = ""),
            Note(id = 2L, title = "B", content = "")
        )
        every { repository.getAllNotes() } returns flowOf(notes)

        val result = useCase().first()

        assertEquals(2, result.size)
        assertEquals("A", result[0].title)
    }

    @Test
    fun `returns empty list when no notes exist`() = runTest {
        every { repository.getAllNotes() } returns flowOf(emptyList())

        val result = useCase().first()

        assertTrue(result.isEmpty())
    }
}

// ─── SyncNotesUseCaseTest ─────────────────────────────────────────────────────

class SyncNotesUseCaseTest {

    private lateinit var repository: NoteRepository
    private lateinit var useCase: SyncNotesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = SyncNotesUseCase(repository)
    }

    @Test
    fun `delegates to repository syncNotes`() = runTest {
        coEvery { repository.syncNotes() } returns Resource.Success(Unit)

        val result = useCase()

        assertTrue(result is Resource.Success)
        coVerify(exactly = 1) { repository.syncNotes() }
    }

    @Test
    fun `propagates partial sync error`() = runTest {
        coEvery { repository.syncNotes() } returns Resource.Error("Partial sync failure: Note 3: HTTP 503")

        val result = useCase()

        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message.contains("Partial sync failure"))
    }
}
