package com.example.notepad.di;

import com.example.notepad.domain.repository.NoteRepository;
import com.example.notepad.domain.usecase.GetNoteByIdUseCase;
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
public final class UseCaseModule_ProvideGetNoteByIdUseCaseFactory implements Factory<GetNoteByIdUseCase> {
  private final Provider<NoteRepository> repoProvider;

  public UseCaseModule_ProvideGetNoteByIdUseCaseFactory(Provider<NoteRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public GetNoteByIdUseCase get() {
    return provideGetNoteByIdUseCase(repoProvider.get());
  }

  public static UseCaseModule_ProvideGetNoteByIdUseCaseFactory create(
      javax.inject.Provider<NoteRepository> repoProvider) {
    return new UseCaseModule_ProvideGetNoteByIdUseCaseFactory(Providers.asDaggerProvider(repoProvider));
  }

  public static UseCaseModule_ProvideGetNoteByIdUseCaseFactory create(
      Provider<NoteRepository> repoProvider) {
    return new UseCaseModule_ProvideGetNoteByIdUseCaseFactory(repoProvider);
  }

  public static GetNoteByIdUseCase provideGetNoteByIdUseCase(NoteRepository repo) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetNoteByIdUseCase(repo));
  }
}
