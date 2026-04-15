package com.example.notepad.data.remote.api

import com.example.notepad.data.remote.dto.DeleteNoteRequest
import com.example.notepad.data.remote.dto.NoteDto
import com.example.notepad.data.remote.dto.UpsertNoteRequest
import retrofit2.Response
import retrofit2.http.*

interface NoteApi {

    @GET("notes")
    suspend fun getAllNotes(): Response<List<NoteDto>>

    @POST("notes")
    suspend fun upsertNote(@Body request: UpsertNoteRequest): Response<NoteDto>

    @HTTP(method = "DELETE", path = "notes", hasBody = true)
    suspend fun deleteNote(@Body request: DeleteNoteRequest): Response<Unit>
}
