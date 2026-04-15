package com.example.notepad.data.remote.api

import com.example.notepad.data.remote.dto.DeleteNoteRequest
import com.example.notepad.data.remote.dto.NoteDto
import com.example.notepad.data.remote.dto.UpsertNoteRequest
import kotlinx.coroutines.delay
import retrofit2.Response

/**
 * A fully in-memory fake implementation of NoteApi.
 *
 * Simulates realistic network latency and can be configured to simulate
 * failures — useful for local dev and UI testing without any real server.
 *
 * Swap this in via Hilt by setting IS_FAKE_API = true in NetworkModule.
 */
class FakeNoteApi : NoteApi {

    // Seed with some realistic starter data so the app isn't empty on first launch
    private val db = mutableMapOf(
        1L to NoteDto(
            id = 1L,
            title = "Welcome to Notepad 👋",
            content = "This is a sample note synced from the server. Swipe left to delete, tap to edit.",
            createdAt = System.currentTimeMillis() - 86_400_000,
            updatedAt = System.currentTimeMillis() - 86_400_000
        ),
        2L to NoteDto(
            id = 2L,
            title = "Offline-first architecture",
            content = "Notes are saved locally first. They sync to the server whenever you're online. The dot on each card shows sync status.",
            createdAt = System.currentTimeMillis() - 3_600_000,
            updatedAt = System.currentTimeMillis() - 3_600_000
        ),
        3L to NoteDto(
            id = 3L,
            title = "Kotlin coroutines + Flow",
            content = "The entire data pipeline — from Room DAO to ViewModel state — is reactive using Kotlin Flow. No polling needed.",
            createdAt = System.currentTimeMillis() - 1_800_000,
            updatedAt = System.currentTimeMillis() - 1_800_000
        )
    )

    private var nextId = 4L

    // Simulate intermittent failures — set to true to test error handling
    var simulateErrors: Boolean = false
    var networkDelayMs: Long = 800L

    override suspend fun getAllNotes(): Response<List<NoteDto>> {
        delay(networkDelayMs)
        if (simulateErrors) return serverError()
        val notes = db.values
            .filter { !it.isDeleted }
            .sortedByDescending { it.updatedAt }
        return Response.success(notes)
    }

    override suspend fun upsertNote(request: UpsertNoteRequest): Response<NoteDto> {
        delay(networkDelayMs)
        if (simulateErrors) return serverError()

        val id = request.id ?: nextId++
        val existing = db[id]
        val dto = NoteDto(
            id = id,
            title = request.title,
            content = request.content,
            createdAt = existing?.createdAt ?: System.currentTimeMillis(),
            updatedAt = request.updatedAt
        )
        db[id] = dto
        return Response.success(dto)
    }

    override suspend fun deleteNote(request: DeleteNoteRequest): Response<Unit> {
        delay(networkDelayMs)
        if (simulateErrors) return serverError()

        db[request.id]?.let {
            db[request.id] = it.copy(isDeleted = true)
        }
        return Response.success(Unit)
    }

    fun reset() {
        db.clear()
        nextId = 1L
    }

    private fun <T> serverError(): Response<T> =
        Response.error(500, okhttp3.ResponseBody.create(null, "Simulated server error"))
}
