package com.example.notepad.data.repository

import com.example.notepad.data.local.dao.NoteDao
import com.example.notepad.data.local.entity.NoteEntity
import com.example.notepad.data.remote.api.NoteApi
import com.example.notepad.data.remote.dto.NoteDto
import com.example.notepad.domain.model.Note
import com.example.notepad.util.Resource
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class NoteRepositoryImplTest {

    private lateinit var dao: NoteDao
    private lateinit var api: NoteApi
    private lateinit var repository: NoteRepositoryImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        api = mockk()
        repository = NoteRepositoryImpl(dao, api)
    }

    @Test
    fun `getAllNotes maps entities to domain models`() = runTest {
        val entities = listOf(
            NoteEntity(id = 1, title = "T", content = "C",
                createdAt = 0, updatedAt = 0, isSynced = true)
        )
        every { dao.observeAllNotes() } returns flowOf(entities)

        val result = repository.getAllNotes().first()
        assertEquals(1, result.size)
        assertEquals("T", result[0].title)
        assertTrue(result[0].isSynced)
    }

    @Test
    fun `upsertNote returns Success when dao succeeds`() = runTest {
        coEvery { dao.upsert(any()) } returns 1L
        val note = Note(title = "Test", content = "Body")

        val result = repository.upsertNote(note)
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `upsertNote returns Error when dao throws`() = runTest {
        coEvery { dao.upsert(any()) } throws RuntimeException("DB error")
        val note = Note(title = "Test", content = "Body")

        val result = repository.upsertNote(note)
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message.contains("DB error"))
    }

    @Test
    fun `fetchRemoteNotes upserts entities on success`() = runTest {
        val dtos = listOf(
            NoteDto(id = 1, title = "Remote", content = "From server",
                createdAt = 0, updatedAt = 0)
        )
        coEvery { api.getAllNotes() } returns Response.success(dtos)

        val result = repository.fetchRemoteNotes()
        assertTrue(result is Resource.Success)
        coVerify { dao.upsertAll(any()) }
    }

    @Test
    fun `fetchRemoteNotes returns Error on HTTP failure`() = runTest {
        coEvery { api.getAllNotes() } returns Response.error(
            500, "Internal server error".toResponseBody()
        )

        val result = repository.fetchRemoteNotes()
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message.contains("500"))
    }

    @Test
    fun `syncNotes marks synced ids after successful push`() = runTest {
        val unsyncedEntity = NoteEntity(
            id = 42, title = "Local", content = "Draft",
            createdAt = 0, updatedAt = 0, isSynced = false
        )
        coEvery { dao.getUnsyncedNotes() } returns listOf(unsyncedEntity)
        coEvery { api.upsertNote(any()) } returns Response.success(
            NoteDto(id = 42, title = "Local", content = "Draft", createdAt = 0, updatedAt = 0)
        )

        val result = repository.syncNotes()
        assertTrue(result is Resource.Success)
        coVerify { dao.markAsSynced(listOf(42L)) }
    }
}
