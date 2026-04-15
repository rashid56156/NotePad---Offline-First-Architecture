package com.example.notepad.data.remote.api

import com.example.notepad.data.remote.dto.DeleteNoteRequest
import com.example.notepad.data.remote.dto.UpsertNoteRequest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FakeNoteApiTest {

    private lateinit var api: FakeNoteApi

    @Before
    fun setup() {
        api = FakeNoteApi()
        api.networkDelayMs = 0L // No delay in tests
    }

    @Test
    fun `getAllNotes returns seeded notes on first call`() = runTest {
        val response = api.getAllNotes()
        assertTrue(response.isSuccessful)
        assertEquals(3, response.body()?.size)
    }

    @Test
    fun `upsertNote with null id creates a new note`() = runTest {
        val request = UpsertNoteRequest(
            id = null,
            title = "New Note",
            content = "Body",
            updatedAt = System.currentTimeMillis()
        )
        val response = api.upsertNote(request)
        assertTrue(response.isSuccessful)
        assertNotNull(response.body()?.id)
        assertEquals("New Note", response.body()?.title)

        // Verify it appears in getAllNotes
        val all = api.getAllNotes().body()!!
        assertTrue(all.any { it.title == "New Note" })
    }

    @Test
    fun `upsertNote with existing id updates the note`() = runTest {
        val request = UpsertNoteRequest(
            id = 1L,
            title = "Updated Title",
            content = "Updated body",
            updatedAt = System.currentTimeMillis()
        )
        val response = api.upsertNote(request)
        assertTrue(response.isSuccessful)
        assertEquals("Updated Title", response.body()?.title)
        assertEquals(1L, response.body()?.id)
    }

    @Test
    fun `deleteNote removes note from getAllNotes result`() = runTest {
        val before = api.getAllNotes().body()!!
        assertTrue(before.any { it.id == 1L })

        api.deleteNote(DeleteNoteRequest(id = 1L))

        val after = api.getAllNotes().body()!!
        assertFalse(after.any { it.id == 1L })
    }

    @Test
    fun `simulateErrors causes getAllNotes to return 500`() = runTest {
        api.simulateErrors = true
        val response = api.getAllNotes()
        assertFalse(response.isSuccessful)
        assertEquals(500, response.code())
    }

    @Test
    fun `simulateErrors causes upsertNote to return 500`() = runTest {
        api.simulateErrors = true
        val response = api.upsertNote(
            UpsertNoteRequest(null, "T", "C", System.currentTimeMillis())
        )
        assertFalse(response.isSuccessful)
        assertEquals(500, response.code())
    }

    @Test
    fun `reset clears all notes`() = runTest {
        api.reset()
        val response = api.getAllNotes()
        assertTrue(response.isSuccessful)
        assertTrue(response.body()!!.isEmpty())
    }
}
