package com.example.notepad.data.mapper

import com.example.notepad.data.local.entity.NoteEntity
import com.example.notepad.data.remote.dto.NoteDto
import com.example.notepad.domain.model.Note
import org.junit.Assert.*
import org.junit.Test

class NoteMapperTest {

    // ─── Entity → Domain ──────────────────────────────────────────────────────

    @Test
    fun `NoteEntity toDomain maps all fields correctly`() {
        val entity = NoteEntity(
            id = 1L, title = "Title", content = "Content",
            createdAt = 1000L, updatedAt = 2000L,
            isSynced = true, isDeleted = false
        )

        val domain = entity.toDomain()

        assertEquals(1L, domain.id)
        assertEquals("Title", domain.title)
        assertEquals("Content", domain.content)
        assertEquals(1000L, domain.createdAt)
        assertEquals(2000L, domain.updatedAt)
        assertTrue(domain.isSynced)
        assertFalse(domain.isDeleted)
    }

    @Test
    fun `NoteEntity toDomain preserves isDeleted = true`() {
        val entity = NoteEntity(
            id = 2L, title = "Deleted", content = "",
            createdAt = 0L, updatedAt = 0L,
            isSynced = false, isDeleted = true
        )
        assertTrue(entity.toDomain().isDeleted)
    }

    // ─── Domain → Entity ──────────────────────────────────────────────────────

    @Test
    fun `Note toEntity maps all fields correctly`() {
        val note = Note(
            id = 3L, title = "T", content = "C",
            createdAt = 100L, updatedAt = 200L,
            isSynced = false, isDeleted = false
        )

        val entity = note.toEntity()

        assertEquals(3L, entity.id)
        assertEquals("T", entity.title)
        assertEquals("C", entity.content)
        assertEquals(100L, entity.createdAt)
        assertEquals(200L, entity.updatedAt)
        assertFalse(entity.isSynced)
        assertFalse(entity.isDeleted)
    }

    @Test
    fun `Entity toDomain then back toEntity is lossless`() {
        val original = NoteEntity(
            id = 10L, title = "Round trip", content = "No data loss",
            createdAt = 999L, updatedAt = 1234L,
            isSynced = true, isDeleted = false
        )

        val roundTripped = original.toDomain().toEntity()

        assertEquals(original, roundTripped)
    }

    // ─── DTO → Domain ─────────────────────────────────────────────────────────

    @Test
    fun `NoteDto toDomain sets isSynced = true always`() {
        val dto = NoteDto(
            id = 5L, title = "Remote", content = "From server",
            createdAt = 500L, updatedAt = 600L, isDeleted = false
        )

        val domain = dto.toDomain()

        assertTrue(domain.isSynced)
        assertEquals(5L, domain.id)
        assertEquals("Remote", domain.title)
    }

    @Test
    fun `NoteDto toDomain preserves isDeleted flag`() {
        val dto = NoteDto(
            id = 6L, title = "Gone", content = "",
            createdAt = 0L, updatedAt = 0L, isDeleted = true
        )

        assertTrue(dto.toDomain().isDeleted)
    }

    @Test
    fun `NoteDto toEntity sets isSynced = true always`() {
        val dto = NoteDto(
            id = 7L, title = "Sync", content = "Body",
            createdAt = 0L, updatedAt = 0L
        )

        val entity = dto.toEntity()

        assertTrue(entity.isSynced)
    }

    // ─── Domain → UpsertRequest ───────────────────────────────────────────────

    @Test
    fun `Note toUpsertRequest sets id = null when id is 0`() {
        val note = Note(id = 0L, title = "New", content = "Note")
        val request = note.toUpsertRequest()
        assertNull(request.id)
    }

    @Test
    fun `Note toUpsertRequest preserves id when non-zero`() {
        val note = Note(id = 42L, title = "Existing", content = "Note")
        val request = note.toUpsertRequest()
        assertEquals(42L, request.id)
    }

    @Test
    fun `Note toUpsertRequest maps title content and updatedAt`() {
        val note = Note(id = 1L, title = "Title", content = "Body", updatedAt = 12345L)
        val request = note.toUpsertRequest()
        assertEquals("Title", request.title)
        assertEquals("Body", request.content)
        assertEquals(12345L, request.updatedAt)
    }
}
