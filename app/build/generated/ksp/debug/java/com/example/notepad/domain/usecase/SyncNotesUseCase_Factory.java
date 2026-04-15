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
public final class SyncNotesUseCase_Factory implements Factory<SyncNotesUseCase> {
  private final Provider<NoteRepository> repositoryProvider;

  public SyncNotesUseCase_Factory(Provider<NoteRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SyncNotesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SyncNotesUseCase_Factory create(
      javax.inject.Provider<NoteRepository> repositoryProvider) {
    return new SyncNotesUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static SyncNotesUseCase_Factory create(Provider<NoteRepository> repositoryProvider) {
    return new SyncNotesUseCase_Factory(repositoryProvider);
  }

  public static SyncNotesUseCase newInstance(NoteRepository repository) {
    return new SyncNotesUseCase(repository);
  }
}
