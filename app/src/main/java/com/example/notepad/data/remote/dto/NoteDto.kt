package com.example.notepad.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NoteDto(
    @SerializedName("id")       val id: Long,
    @SerializedName("title")    val title: String,
    @SerializedName("content")  val content: String,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("updated_at") val updatedAt: Long,
    @SerializedName("is_deleted") val isDeleted: Boolean = false
)

data class UpsertNoteRequest(
    @SerializedName("id")       val id: Long?,
    @SerializedName("title")    val title: String,
    @SerializedName("content")  val content: String,
    @SerializedName("updated_at") val updatedAt: Long
)

data class DeleteNoteRequest(
    @SerializedName("id") val id: Long
)
