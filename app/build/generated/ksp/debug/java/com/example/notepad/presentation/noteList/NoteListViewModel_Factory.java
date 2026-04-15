package com.example.notepad.presentation.noteList;

import com.example.notepad.domain.usecase.DeleteNoteUseCase;
import com.example.notepad.domain.usecase.FetchRemoteNotesUseCase;
import com.example.notepad.domain.usecase.GetAllNotesUseCase;
import com.example.notepad.domain.usecase.SyncNotesUseCase;
import com.example.notepad.util.NetworkMonitor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class NoteListViewModel_Factory implements Factory<NoteListViewModel> {
  private final Provider<GetAllNotesUseCase> getAllNotesUseCaseProvider;

  private final Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider;

  private final Provider<SyncNotesUseCase> syncNotesUseCaseProvider;

  private final Provider<FetchRemoteNotesUseCase> fetchRemoteNotesUseCaseProvider;

  private final Provider<NetworkMonitor> networkMonitorProvider;

  public NoteListViewModel_Factory(Provider<GetAllNotesUseCase> getAllNotesUseCaseProvider,
      Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider,
      Provider<SyncNotesUseCase> syncNotesUseCaseProvider,
      Provider<FetchRemoteNotesUseCase> fetchRemoteNotesUseCaseProvider,
      Provider<NetworkMonitor> networkMonitorProvider) {
    this.getAllNotesUseCaseProvider = getAllNotesUseCaseProvider;
    this.deleteNoteUseCaseProvider = deleteNoteUseCaseProvider;
    this.syncNotesUseCaseProvider = syncNotesUseCaseProvider;
    this.fetchRemoteNotesUseCaseProvider = fetchRemoteNotesUseCaseProvider;
    this.networkMonitorProvider = networkMonitorProvider;
  }

  @Override
  public NoteListViewModel get() {
    return newInstance(getAllNotesUseCaseProvider.get(), deleteNoteUseCaseProvider.get(), syncNotesUseCaseProvider.get(), fetchRemoteNotesUseCaseProvider.get(), networkMonitorProvider.get());
  }

  public static NoteListViewModel_Factory create(
      javax.inject.Provider<GetAllNotesUseCase> getAllNotesUseCaseProvider,
      javax.inject.Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider,
      javax.inject.Provider<SyncNotesUseCase> syncNotesUseCaseProvider,
      javax.inject.Provider<FetchRemoteNotesUseCase> fetchRemoteNotesUseCaseProvider,
      javax.inject.Provider<NetworkMonitor> networkMonitorProvider) {
    return new NoteListViewModel_Factory(Providers.asDaggerProvider(getAllNotesUseCaseProvider), Providers.asDaggerProvider(deleteNoteUseCaseProvider), Providers.asDaggerProvider(syncNotesUseCaseProvider), Providers.asDaggerProvider(fetchRemoteNotesUseCaseProvider), Providers.asDaggerProvider(networkMonitorProvider));
  }

  public static NoteListViewModel_Factory create(
      Provider<GetAllNotesUseCase> getAllNotesUseCaseProvider,
      Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider,
      Provider<SyncNotesUseCase> syncNotesUseCaseProvider,
      Provider<FetchRemoteNotesUseCase> fetchRemoteNotesUseCaseProvider,
      Provider<NetworkMonitor> networkMonitorProvider) {
    return new NoteListViewModel_Factory(getAllNotesUseCaseProvider, deleteNoteUseCaseProvider, syncNotesUseCaseProvider, fetchRemoteNotesUseCaseProvider, networkMonitorProvider);
  }

  public static NoteListViewModel newInstance(GetAllNotesUseCase getAllNotesUseCase,
      DeleteNoteUseCase deleteNoteUseCase, SyncNotesUseCase syncNotesUseCase,
      FetchRemoteNotesUseCase fetchRemoteNotesUseCase, NetworkMonitor networkMonitor) {
    return new NoteListViewModel(getAllNotesUseCase, deleteNoteUseCase, syncNotesUseCase, fetchRemoteNotesUseCase, networkMonitor);
  }
}
