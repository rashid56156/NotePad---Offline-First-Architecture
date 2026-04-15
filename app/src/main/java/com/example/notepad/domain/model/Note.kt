package com.example.notepad.domain.model

/**
 * Pure domain model. No Android or framework dependencies.
 * This is the single source of truth for what a Note is in the app.
 */
data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
)
