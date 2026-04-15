package com.example.notepad.domain.usecase;

import com.example.notepad.domain.repository.NoteRepository;
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
public final class UpsertNoteUseCase_Factory implements Factory<UpsertNoteUseCase> {
  private final Provider<NoteRepository> repositoryProvider;

  public UpsertNoteUseCase_Factory(Provider<NoteRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpsertNoteUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpsertNoteUseCase_Factory create(
      javax.inject.Provider<NoteRepository> repositoryProvider) {
    return new UpsertNoteUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static UpsertNoteUseCase_Factory create(Provider<NoteRepository> repositoryProvider) {
    return new UpsertNoteUseCase_Factory(repositoryProvider);
  }

  public static UpsertNoteUseCase newInstance(NoteRepository repository) {
    return new UpsertNoteUseCase(repository);
  }
}
