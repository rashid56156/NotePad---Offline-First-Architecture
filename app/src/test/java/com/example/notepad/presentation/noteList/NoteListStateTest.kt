package com.example.notepad.presentation.noteList

import com.example.notepad.domain.model.Note
import org.junit.Assert.*
import org.junit.Test

/**
 * Pure unit tests for NoteListState computed properties.
 * No coroutines, no mocks — just plain Kotlin logic.
 */
class NoteListStateTest {

    private val sampleNotes = listOf(
        Note(id = 1L, title = "Shopping list", content = "Milk, eggs, bread"),
        Note(id = 2L, title = "Meeting notes", content = "Q3 OKRs discussed"),
        Note(id = 3L, title = "Kotlin tips", content = "Use sealed classes for state"),
        Note(id = 4L, title = "Grocery run", content = "Also need milk")
    )

    @Test
    fun `filteredNotes returns all notes when searchQuery is blank`() {
        val state = NoteListState(notes = sampleNotes, searchQuery = "")
        assertEquals(4, state.filteredNotes.size)
    }

    @Test
    fun `filteredNotes returns all notes when searchQuery is whitespace`() {
        val state = NoteListState(notes = sampleNotes, searchQuery = "   ")
        assertEquals(4, state.filteredNotes.size)
    }

    @Test
    fun `filteredNotes filters by title case-insensitively`() {
        val state = NoteListState(notes = sampleNotes, searchQuery = "KOTLIN")
        assertEquals(1, state.filteredNotes.size)
        assertEquals("Kotlin tips", state.filteredNotes[0].title)
    }

    @Test
    fun `filteredNotes filters by content case-insensitively`() {
        val state = NoteListState(notes = sampleNotes, searchQuery = "milk")
        // matches "Shopping list" (content) and "Grocery run" (content)
        assertEquals(2, state.filteredNotes.size)
    }

    @Test
    fun `filteredNotes returns empty list when nothing matches`() {
        val state = NoteListState(notes = sampleNotes, searchQuery = "zzznotfound")
        assertTrue(state.filteredNotes.isEmpty())
    }

    @Test
    fun `filteredNotes matches partial query in title`() {
        val state = NoteListState(notes = sampleNotes, searchQuery = "meet")
        assertEquals(1, state.filteredNotes.size)
        assertEquals("Meeting notes", state.filteredNotes[0].title)
    }

    @Test
    fun `filteredNotes returns empty list when notes are empty`() {
        val state = NoteListState(notes = emptyList(), searchQuery = "anything")
        assertTrue(state.filteredNotes.isEmpty())
    }

    @Test
    fun `initial state has correct defaults`() {
        val state = NoteListState()
        assertTrue(state.notes.isEmpty())
        assertFalse(state.isLoading)
        assertFalse(state.isSyncing)
        assertNull(state.error)
        assertNull(state.syncMessage)
        assertEquals("", state.searchQuery)
        assertFalse(state.isOnline)
    }
}
