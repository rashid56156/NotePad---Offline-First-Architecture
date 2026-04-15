package com.example.notepad.data.mapper

import com.example.notepad.data.local.entity.NoteEntity
import com.example.notepad.data.remote.dto.NoteDto
import com.example.notepad.data.remote.dto.UpsertNoteRequest
import com.example.notepad.domain.model.Note

// ─── Entity ↔ Domain ─────────────────────────────────────────────────────────

fun NoteEntity.toDomain(): Note = Note(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isSynced = isSynced,
    isDeleted = isDeleted
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isSynced = isSynced,
    isDeleted = isDeleted
)

// ─── DTO ↔ Domain ─────────────────────────────────────────────────────────────

fun NoteDto.toDomain(): Note = Note(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isSynced = true,
    isDeleted = isDeleted
)

fun NoteDto.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isSynced = true,
    isDeleted = isDeleted
)

// ─── Domain → Request ─────────────────────────────────────────────────────────

fun Note.toUpsertRequest(): UpsertNoteRequest = UpsertNoteRequest(
    id = if (id == 0L) null else id,
    title = title,
    content = content,
    updatedAt = updatedAt
)
