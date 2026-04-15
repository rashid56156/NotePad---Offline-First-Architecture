package com.example.notepad.di;

import com.example.notepad.domain.repository.NoteRepository;
import com.example.notepad.domain.usecase.FetchRemoteNotesUseCase;
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
public final class UseCaseModule_ProvideFetchRemoteNotesUseCaseFactory implements Factory<FetchRemoteNotesUseCase> {
  private final Provider<NoteRepository> repoProvider;

  public UseCaseModule_ProvideFetchRemoteNotesUseCaseFactory(
      Provider<NoteRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public FetchRemoteNotesUseCase get() {
    return provideFetchRemoteNotesUseCase(repoProvider.get());
  }

  public static UseCaseModule_ProvideFetchRemoteNotesUseCaseFactory create(
      javax.inject.Provider<NoteRepository> repoProvider) {
    return new UseCaseModule_ProvideFetchRemoteNotesUseCaseFactory(Providers.asDaggerProvider(repoProvider));
  }

  public static UseCaseModule_ProvideFetchRemoteNotesUseCaseFactory create(
      Provider<NoteRepository> repoProvider) {
    return new UseCaseModule_ProvideFetchRemoteNotesUseCaseFactory(repoProvider);
  }

  public static FetchRemoteNotesUseCase provideFetchRemoteNotesUseCase(NoteRepository repo) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideFetchRemoteNotesUseCase(repo));
  }
}
