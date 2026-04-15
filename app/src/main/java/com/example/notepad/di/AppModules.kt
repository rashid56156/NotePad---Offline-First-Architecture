package com.example.notepad.di

import android.content.Context
import androidx.room.Room
import com.example.notepad.data.local.dao.NoteDao
import com.example.notepad.data.local.database.NoteDatabase
import com.example.notepad.data.remote.api.NoteApi
import com.example.notepad.data.repository.NoteRepositoryImpl
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// ─── Database Module ──────────────────────────────────────────────────────────

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao
}

// ─── Repository Module ────────────────────────────────────────────────────────

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        dao: NoteDao,
        api: NoteApi
    ): NoteRepository = NoteRepositoryImpl(dao, api)
}

// ─── UseCase Module ───────────────────────────────────────────────────────────

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetAllNotesUseCase(repo: NoteRepository) = GetAllNotesUseCase(repo)

    @Provides
    fun provideGetNoteByIdUseCase(repo: NoteRepository) = GetNoteByIdUseCase(repo)

    @Provides
    fun provideUpsertNoteUseCase(repo: NoteRepository) = UpsertNoteUseCase(repo)

    @Provides
    fun provideDeleteNoteUseCase(repo: NoteRepository) = DeleteNoteUseCase(repo)

    @Provides
    fun provideSyncNotesUseCase(repo: NoteRepository) = SyncNotesUseCase(repo)

    @Provides
    fun provideFetchRemoteNotesUseCase(repo: NoteRepository) = FetchRemoteNotesUseCase(repo)
}
