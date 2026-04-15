package com.example.notepad.domain.usecase

import com.example.notepad.domain.model.Note
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.util.Resource
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UpsertNoteUseCaseTest {

    private lateinit var repository: NoteRepository
    private lateinit var useCase: UpsertNoteUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpsertNoteUseCase(repository)
    }

    @Test
    fun `returns Error when title and content are both blank`() = runTest {
        val note = Note(title = "  ", content = "")
        val result = useCase(note)
        assertTrue(result is Resource.Error)
        assertEquals("Note cannot be empty.", (result as Resource.Error).message)
        coVerify(exactly = 0) { repository.upsertNote(any()) }
    }

    @Test
    fun `trims whitespace and sets isSynced to false before saving`() = runTest {
        val note = Note(title = "  Hello  ", content = "  World  ", isSynced = true)
        val slot = slot<Note>()
        coEvery { repository.upsertNote(capture(slot)) } returns Resource.Success(Unit)

        useCase(note)

        assertEquals("Hello", slot.captured.title)
        assertEquals("World", slot.captured.content)
        assertFalse(slot.captured.isSynced)
    }

    @Test
    fun `delegates to repository when note is valid`() = runTest {
        val note = Note(title = "Valid", content = "Content")
        coEvery { repository.upsertNote(any()) } returns Resource.Success(Unit)

        val result = useCase(note)
        assertTrue(result is Resource.Success)
        coVerify(exactly = 1) { repository.upsertNote(any()) }
    }

    @Test
    fun `note with blank title but non-blank content is valid`() = runTest {
        val note = Note(title = "", content = "Just content")
        coEvery { repository.upsertNote(any()) } returns Resource.Success(Unit)

        val result = useCase(note)
        assertTrue(result is Resource.Success)
    }
}
