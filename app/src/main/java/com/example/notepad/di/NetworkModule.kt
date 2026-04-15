package com.example.notepad.di

import com.example.notepad.data.remote.api.FakeNoteApi
import com.example.notepad.data.remote.api.NoteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Toggle this to switch between the real Retrofit API and the in-memory fake.
     *
     * true  → uses FakeNoteApi (no server needed, works offline out of the box)
     * false → uses real Retrofit API pointed at BASE_URL
     */
    private const val USE_FAKE_API = true

    private const val BASE_URL = "https://your-api.example.com/api/v1/"

    @Provides
    @Singleton
    fun provideNoteApi(retrofit: Retrofit): NoteApi =
        if (USE_FAKE_API) FakeNoteApi()
        else retrofit.create(NoteApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}
