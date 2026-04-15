package com.example.notepad.di;

import com.example.notepad.domain.repository.NoteRepository;
import com.example.notepad.domain.usecase.SyncNotesUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class UseCaseModule_ProvideSyncNotesUseCaseFactory implements Factory<SyncNotesUseCase> {
  private final Provider<NoteRepository> repoProvider;

  public UseCaseModule_ProvideSyncNotesUseCaseFactory(Provider<NoteRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public SyncNotesUseCase get() {
    return provideSyncNotesUseCase(repoProvider.get());
  }

  public static UseCaseModule_ProvideSyncNotesUseCaseFactory create(
      javax.inject.Provider<NoteRepository> repoProvider) {
    return new UseCaseModule_ProvideSyncNotesUseCaseFactory(Providers.asDaggerProvider(repoProvider));
  }

  public static UseCaseModule_ProvideSyncNotesUseCaseFactory create(
      Provider<NoteRepository> repoProvider) {
    return new UseCaseModule_ProvideSyncNotesUseCaseFactory(repoProvider);
  }

  public static SyncNotesUseCase provideSyncNotesUseCase(NoteRepository repo) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideSyncNotesUseCase(repo));
  }
}
