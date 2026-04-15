package com.example.notepad.di;

import com.example.notepad.domain.repository.NoteRepository;
import com.example.notepad.domain.usecase.GetAllNotesUseCase;
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
public final class UseCaseModule_ProvideGetAllNotesUseCaseFactory implements Factory<GetAllNotesUseCase> {
  private final Provider<NoteRepository> repoProvider;

  public UseCaseModule_ProvideGetAllNotesUseCaseFactory(Provider<NoteRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public GetAllNotesUseCase get() {
    return provideGetAllNotesUseCase(repoProvider.get());
  }

  public static UseCaseModule_ProvideGetAllNotesUseCaseFactory create(
      javax.inject.Provider<NoteRepository> repoProvider) {
    return new UseCaseModule_ProvideGetAllNotesUseCaseFactory(Providers.asDaggerProvider(repoProvider));
  }

  public static UseCaseModule_ProvideGetAllNotesUseCaseFactory create(
      Provider<NoteRepository> repoProvider) {
    return new UseCaseModule_ProvideGetAllNotesUseCaseFactory(repoProvider);
  }

  public static GetAllNotesUseCase provideGetAllNotesUseCase(NoteRepository repo) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetAllNotesUseCase(repo));
  }
}
