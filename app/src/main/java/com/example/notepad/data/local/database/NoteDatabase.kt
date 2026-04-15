package com.example.notepad.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notepad.data.local.dao.NoteDao
import com.example.notepad.data.local.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = true
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}
