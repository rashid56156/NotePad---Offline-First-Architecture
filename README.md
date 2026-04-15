<div align="center">

<img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
<img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
<img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>
<img src="https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-FF6F00?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Min%20SDK-26-brightgreen?style=for-the-badge"/>
<img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge"/>

<br/>
<br/>

# 📝 Notepad — Offline-First Android App

**A production-grade, fully offline-capable notepad application built with modern Android architecture.**  
Write, edit, and delete notes without an internet connection. Your data syncs automatically the moment you're back online.

<br/>

[Features](#-features) • [Architecture](#-architecture) • [Tech Stack](#-tech-stack) • [Screenshots](#-screenshots) • [Getting Started](#-getting-started) • [Project Structure](#-project-structure) • [Testing](#-testing) • [Future Enhancements](#-future-enhancements)

</div>

---

## 📖 Overview

Notepad is a showcase Android application demonstrating **offline-first architecture** using the latest Android development stack. Every note you create is saved instantly to a local Room database — no network required. In the background, a sync engine pushes unsynced changes to a remote server whenever connectivity is available, and pulls the latest remote data back down.

This project is designed as a **reference implementation** for:

- Offline-first data flow with Room + Retrofit
- MVVM + Clean Architecture with strict layer separation
- Reactive UI using Kotlin Flow + Jetpack Compose
- Dependency injection with Hilt
- Sealed classes for type-safe state management
- Unit testing with MockK, Turbine, and Coroutine Test

Whether you're learning modern Android development or looking for a solid architectural foundation to build upon, this project covers all the patterns you'd expect in a real-world production app.

---

## ✨ Features

| Feature | Description |
|---|---|
| ✅ **Create & Edit Notes** | Rich text editing with auto-save on back navigation |
| ✅ **Offline First** | All reads and writes go to Room first — zero network dependency |
| ✅ **Auto Sync** | Unsynced notes push to server automatically when connectivity is restored |
| ✅ **Manual Sync** | Tap the sync button to trigger a push/pull at any time |
| ✅ **Sync Status Indicator** | Green dot = synced, amber dot = pending — visible on every card |
| ✅ **Connectivity Banner** | A non-intrusive banner appears when the device goes offline |
| ✅ **Search** | Real-time full-text search across note titles and content |
| ✅ **Swipe to Delete** | Swipe left on any card to reveal the delete action |
| ✅ **Delete Confirmation** | Bottom sheet dialog prevents accidental deletions |
| ✅ **Soft Delete** | Deletions are synced to the server before being permanently purged |
| ✅ **Dark Mode** | Full Material 3 dark theme support |
| ✅ **Dynamic Color** | Android 12+ wallpaper-based color extraction |
| ✅ **Fake API Mode** | Toggle between real Retrofit and an in-memory fake with one flag |

---

## 🏛️ Architecture

This app follows **Clean Architecture** principles with **MVVM** in the presentation layer, organized into three strict layers that only communicate in one direction.

```
┌─────────────────────────────────────────────────────┐
│                  Presentation Layer                  │
│         Compose UI · ViewModel · StateFlow          │
│         Sealed States · Sealed Events · Intents     │
└────────────────────────┬────────────────────────────┘
                         │ calls
┌────────────────────────▼────────────────────────────┐
│                   Domain Layer                       │
│        Use Cases · Repository Interface             │
│        Domain Models · Business Rules               │
└────────────────────────┬────────────────────────────┘
                         │ implements
┌────────────────────────▼────────────────────────────┐
│                    Data Layer                        │
│     Room (local) · Retrofit (remote) · Mappers      │
│     Repository Impl · Offline-First Sync Logic      │
└─────────────────────────────────────────────────────┘
```

### Offline-First Data Flow

```
User Action
    │
    ▼
ViewModel.onIntent()
    │
    ▼
UseCase (validates + transforms)
    │
    ▼
NoteRepositoryImpl
    ├──► Room (write immediately, isSynced = false)
    │        │
    │        └──► StateFlow emits ──► UI updates instantly
    │
    └──► Sync Engine (background, when online)
             │
             ├──► POST/DELETE to Remote API
             │
             └──► markAsSynced(ids) in Room
```

**The golden rule:** The UI never waits for a network call. Every write succeeds locally in milliseconds. Network sync is a background concern that the user never has to think about.

### State Management

Each screen has three sealed classes:

```kotlin
// What the UI renders
data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isSyncing: Boolean = false,
    val isOnline: Boolean = false,
    ...
)

// One-time side effects (navigation, snackbars)
sealed class NoteListEvent {
    data class NavigateToDetail(val noteId: Long?) : NoteListEvent()
    data class ShowSnackbar(val message: String) : NoteListEvent()
}

// User actions dispatched to ViewModel
sealed class NoteListIntent {
    data object CreateNote : NoteListIntent()
    data class DeleteNote(val noteId: Long) : NoteListIntent()
    data class SearchQueryChanged(val query: String) : NoteListIntent()
    ...
}
```

`StateFlow` carries the UI state. `Channel` carries one-time events. Intents are the only entry point into the ViewModel — no public mutable state is exposed.

---

## 🛠️ Tech Stack

### Core

| Technology | Purpose |
|---|---|
| **Kotlin** | Primary language |
| **Kotlin Coroutines** | Asynchronous programming, background work |
| **Kotlin Flow** | Reactive data streams from DB to UI |
| **Sealed Classes** | Type-safe state, events, and intents |

### UI

| Technology | Purpose |
|---|---|
| **Jetpack Compose** | Declarative UI toolkit |
| **Material 3** | Design system with dynamic color |
| **Navigation Compose** | Type-safe screen navigation |
| **Compose Animation** | `AnimatedVisibility`, `animateItem()` |

### Architecture & DI

| Technology | Purpose |
|---|---|
| **MVVM + Clean Architecture** | Separation of concerns across 3 layers |
| **Hilt** | Compile-time dependency injection |
| **ViewModel** | UI state holder, survives config changes |
| **SavedStateHandle** | Argument passing + process death survival |

### Data

| Technology | Purpose |
|---|---|
| **Room** | Local SQLite persistence with Flow support |
| **Retrofit** | Type-safe HTTP client for remote API |
| **OkHttp** | HTTP layer with logging interceptor |
| **Gson** | JSON serialization/deserialization |
| **DataStore** | Sync metadata and preferences |

### Testing

| Technology | Purpose |
|---|---|
| **JUnit 4** | Test runner |
| **MockK** | Kotlin-native mocking with coroutine support |
| **Turbine** | Flow and Channel testing |
| **kotlinx-coroutines-test** | Virtual time control with `StandardTestDispatcher` |

---

## 📸 Screenshots

> The app ships with a `FakeNoteApi` that seeds 3 notes on first launch, so you can see the full experience without a backend.

| Note List | Note Detail | New Note | Empty State |
|---|---|---|---|
| Main feed with sync dots and search | Full-screen editor with word count | Blank editor with auto-focus | First-time user experience |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio **Hedgehog (2023.1.1)** or later
- JDK **17**
- Android SDK **35** (targetSdk)
- Minimum device/emulator: **Android 8.0 (API 26)**

### Installation

**1. Clone the repository**
```bash
git clone https://github.com/rashid56156/NotePad---Offline-First-Architecture.git
cd NotePad---Offline-First-Architecture
```

**2. Open in Android Studio**

`File → Open → select the project root folder`

Android Studio detects `settings.gradle.kts` and begins syncing automatically.

**3. Run the app**

The app ships with a **fake in-memory API** enabled by default — no backend required.

```kotlin
// di/NetworkModule.kt
private const val USE_FAKE_API = true  // ← already set to true
```

Press **Run ▶** and the app launches immediately with 3 seeded notes.

### Switching to a Real Backend

```kotlin
// di/NetworkModule.kt
private const val USE_FAKE_API = false
private const val BASE_URL = "https://your-api.example.com/api/v1/"
```

Your backend must implement these three endpoints:

```
GET    /notes              → List<NoteDto>
POST   /notes              → NoteDto          (body: UpsertNoteRequest)
DELETE /notes              → 200 OK           (body: DeleteNoteRequest)
```

> **Note:** On first sync after cloning, Android Studio may prompt `"Gradle wrapper jar is missing"`. Click **OK** — it downloads automatically. Alternatively run `gradle wrapper` from the terminal once.

---

## 📁 Project Structure

```
app/
└── src/main/java/com/example/notepad/
    │
    ├── data/                          # Data layer
    │   ├── local/
    │   │   ├── dao/NoteDao.kt         # Room queries (Flow-based)
    │   │   ├── database/NoteDatabase.kt
    │   │   └── entity/NoteEntity.kt   # Room table definition
    │   ├── remote/
    │   │   ├── api/NoteApi.kt         # Retrofit interface
    │   │   ├── api/FakeNoteApi.kt     # In-memory fake for dev/testing
    │   │   └── dto/NoteDto.kt         # JSON data transfer objects
    │   ├── mapper/NoteMapper.kt       # Entity ↔ Domain ↔ DTO conversions
    │   └── repository/
    │       └── NoteRepositoryImpl.kt  # Offline-first sync logic
    │
    ├── domain/                        # Domain layer (pure Kotlin, no Android deps)
    │   ├── model/Note.kt              # Core domain model
    │   ├── repository/NoteRepository.kt  # Repository contract (interface)
    │   └── usecase/NoteUseCases.kt    # Business logic (5 use cases)
    │
    ├── presentation/                  # Presentation layer
    │   ├── noteList/
    │   │   ├── NoteListScreen.kt      # Composable UI
    │   │   ├── NoteListViewModel.kt
    │   │   └── NoteListState.kt       # State + Event + Intent sealed classes
    │   ├── noteDetail/
    │   │   ├── NoteDetailScreen.kt
    │   │   ├── NoteDetailViewModel.kt
    │   │   └── NoteDetailState.kt
    │   ├── components/NoteCard.kt     # Reusable swipeable card
    │   └── navigation/NoteNavGraph.kt # Compose Navigation graph
    │
    ├── di/                            # Hilt modules
    │   ├── AppModules.kt              # Database, Repository, UseCase modules
    │   └── NetworkModule.kt           # Retrofit / FakeApi toggle
    │
    ├── ui/theme/Theme.kt              # Material 3 + dynamic color theme
    ├── util/
    │   ├── Resource.kt                # Sealed result wrapper (Success/Error/Loading)
    │   └── NetworkMonitor.kt          # Connectivity Flow via ConnectivityManager
    │
    ├── MainActivity.kt
    └── NotepadApplication.kt          # @HiltAndroidApp entry point
```

---

## 🧪 Testing

The project has **9 test files** covering all architectural layers with ~80 test cases.

### Run all unit tests
```bash
./gradlew test
```

### Test coverage by layer

| Test File | Layer | What it covers |
|---|---|---|
| `ResourceTest` | Util | All sealed class states, helper functions |
| `NoteMapperTest` | Data | Every mapping direction, round-trip losslessness |
| `UpsertNoteUseCaseTest` | Domain | Blank note validation, whitespace trimming, sync flag |
| `OtherUseCaseTests` | Domain | Delete, GetById, GetAll, Sync use cases |
| `NoteRepositoryImplTest` | Data | Room mapping, HTTP success/failure, sync ID marking |
| `NoteListStateTest` | Presentation | `filteredNotes` search logic, computed properties |
| `NoteListViewModelTest` | Presentation | State updates, delete events, navigation intents |
| `NoteDetailViewModelTest` | Presentation | Load/save/delete flows, `isSaving` timing, BackHandler |
| `FakeNoteApiTest` | Data | CRUD ops, error simulation, seeded data, reset |

### Testing philosophy

- **MockK** is used over Mockito for native Kotlin coroutine support (`coEvery`, `coVerify`)
- **Turbine** replaces fragile `runBlocking { flow.first() }` patterns with clean `test { awaitItem() }` assertions for both `StateFlow` and `Channel`-backed event streams
- **`StandardTestDispatcher`** with `advanceUntilIdle()` gives full control over coroutine execution timing in ViewModel tests
- Business logic (use cases) is tested independently from the repository and ViewModel — each layer is verified in isolation

---

## 🔮 Future Enhancements

### Near-term

- [ ] **Rich text editor** — Bold, italic, bullet lists using a Compose-compatible rich text library
- [ ] **Note pinning** — Pin important notes to the top of the list
- [ ] **Color labels** — Tag notes with colors for visual organization
- [ ] **Note sharing** — Share note content via the Android share sheet
- [ ] **Undo delete** — Snackbar with undo action using a grace period before final deletion

### Medium-term

- [ ] **Folders / Categories** — Organize notes into user-defined groups
- [ ] **Image attachments** — Attach photos from gallery or camera
- [ ] **Voice notes** — Record and attach audio clips to notes
- [ ] **Markdown support** — Render markdown in a preview mode
- [ ] **WorkManager sync** — Replace manual sync with `PeriodicWorkRequest` for guaranteed background sync
- [ ] **Conflict resolution UI** — When a remote and local note diverge, show a diff view and let the user choose

### Long-term

- [ ] **Multi-device support** — Full account system with JWT authentication
- [ ] **End-to-end encryption** — Encrypt note content on-device before syncing
- [ ] **Widgets** — Android home screen widget for quick note creation
- [ ] **Wear OS companion** — View and dictate notes from a smartwatch
- [ ] **Backup to Google Drive** — Manual and automatic backup to the user's Drive
- [ ] **Tablet / foldable layout** — Two-pane adaptive layout using `WindowSizeClass`

### Technical debt / improvements

- [ ] **Room migrations** — Replace `fallbackToDestructiveMigration()` with proper migration scripts
- [ ] **Paging 3** — Paginate the notes list for users with thousands of notes
- [ ] **Baseline Profiles** — Ship a Baseline Profile to improve startup and scroll performance
- [ ] **Screenshot tests** — Add Paparazzi-based screenshot testing for Compose UI
- [ ] **CI/CD pipeline** — GitHub Actions workflow for automated build, test, and lint

---

## 📦 Dependencies Reference

```toml
# gradle/libs.versions.toml

[versions]
kotlin                = "2.1.0"
agp                   = "8.7.3"
ksp                   = "2.1.0-1.0.29"
composeBom            = "2024.12.01"
room                  = "2.6.1"
hilt                  = "2.54"
hiltNavigation        = "1.2.0"
retrofit              = "2.11.0"
okhttp                = "4.12.0"
coroutines            = "1.9.0"
navigationCompose     = "2.8.5"
lifecycleRuntimeKtx   = "2.8.7"

# Testing
turbine               = "1.2.0"
mockk                 = "1.13.12"
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Ensure all tests pass (`./gradlew test`)
5. Push to the branch (`git push origin feature/my-feature`)
6. Open a Pull Request

Please keep the architecture consistent — new features should respect the domain/data/presentation layer boundaries.

---

## 📄 License

```
MIT License

Copyright (c) 2024 Your Name

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

<div align="center">

Built with ❤️ using modern Android development best practices

**[⬆ Back to top](#-notepad--offline-first-android-app)**

</div>
