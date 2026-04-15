package com.example.notepad.di;

import com.example.notepad.domain.repository.NoteRepository;
import com.example.notepad.domain.usecase.DeleteNoteUseCase;
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
public final class UseCaseModule_ProvideDeleteNoteUseCaseFactory implements Factory<DeleteNoteUseCase> {
  private final Provider<NoteRepository> repoProvider;

  public UseCaseModule_ProvideDeleteNoteUseCaseFactory(Provider<NoteRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public DeleteNoteUseCase get() {
    return provideDeleteNoteUseCase(repoProvider.get());
  }

  public static UseCaseModule_ProvideDeleteNoteUseCaseFactory create(
      javax.inject.Provider<NoteRepository> repoProvider) {
    return new UseCaseModule_ProvideDeleteNoteUseCaseFactory(Providers.asDaggerProvider(repoProvider));
  }

  public static UseCaseModule_ProvideDeleteNoteUseCaseFactory create(
      Provider<NoteRepository> repoProvider) {
    return new UseCaseModule_ProvideDeleteNoteUseCaseFactory(repoProvider);
  }

  public static DeleteNoteUseCase provideDeleteNoteUseCase(NoteRepository repo) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideDeleteNoteUseCase(repo));
  }
}
