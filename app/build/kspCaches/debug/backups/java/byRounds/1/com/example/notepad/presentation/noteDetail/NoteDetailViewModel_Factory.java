package com.example.notepad.presentation.noteDetail;

import androidx.lifecycle.SavedStateHandle;
import com.example.notepad.domain.usecase.DeleteNoteUseCase;
import com.example.notepad.domain.usecase.GetNoteByIdUseCase;
import com.example.notepad.domain.usecase.UpsertNoteUseCase;
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
public final class NoteDetailViewModel_Factory implements Factory<NoteDetailViewModel> {
  private final Provider<GetNoteByIdUseCase> getNoteByIdUseCaseProvider;

  private final Provider<UpsertNoteUseCase> upsertNoteUseCaseProvider;

  private final Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public NoteDetailViewModel_Factory(Provider<GetNoteByIdUseCase> getNoteByIdUseCaseProvider,
      Provider<UpsertNoteUseCase> upsertNoteUseCaseProvider,
      Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.getNoteByIdUseCaseProvider = getNoteByIdUseCaseProvider;
    this.upsertNoteUseCaseProvider = upsertNoteUseCaseProvider;
    this.deleteNoteUseCaseProvider = deleteNoteUseCaseProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public NoteDetailViewModel get() {
    return newInstance(getNoteByIdUseCaseProvider.get(), upsertNoteUseCaseProvider.get(), deleteNoteUseCaseProvider.get(), savedStateHandleProvider.get());
  }

  public static NoteDetailViewModel_Factory create(
      javax.inject.Provider<GetNoteByIdUseCase> getNoteByIdUseCaseProvider,
      javax.inject.Provider<UpsertNoteUseCase> upsertNoteUseCaseProvider,
      javax.inject.Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider,
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider) {
    return new NoteDetailViewModel_Factory(Providers.asDaggerProvider(getNoteByIdUseCaseProvider), Providers.asDaggerProvider(upsertNoteUseCaseProvider), Providers.asDaggerProvider(deleteNoteUseCaseProvider), Providers.asDaggerProvider(savedStateHandleProvider));
  }

  public static NoteDetailViewModel_Factory create(
      Provider<GetNoteByIdUseCase> getNoteByIdUseCaseProvider,
      Provider<UpsertNoteUseCase> upsertNoteUseCaseProvider,
      Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new NoteDetailViewModel_Factory(getNoteByIdUseCaseProvider, upsertNoteUseCaseProvider, deleteNoteUseCaseProvider, savedStateHandleProvider);
  }

  public static NoteDetailViewModel newInstance(GetNoteByIdUseCase getNoteByIdUseCase,
      UpsertNoteUseCase upsertNoteUseCase, DeleteNoteUseCase deleteNoteUseCase,
      SavedStateHandle savedStateHandle) {
    return new NoteDetailViewModel(getNoteByIdUseCase, upsertNoteUseCase, deleteNoteUseCase, savedStateHandle);
  }
}
