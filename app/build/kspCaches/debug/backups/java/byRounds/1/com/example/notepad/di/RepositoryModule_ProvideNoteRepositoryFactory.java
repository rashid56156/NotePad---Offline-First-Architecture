package com.example.notepad.di;

import com.example.notepad.data.local.dao.NoteDao;
import com.example.notepad.data.remote.api.NoteApi;
import com.example.notepad.domain.repository.NoteRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class RepositoryModule_ProvideNoteRepositoryFactory implements Factory<NoteRepository> {
  private final Provider<NoteDao> daoProvider;

  private final Provider<NoteApi> apiProvider;

  public RepositoryModule_ProvideNoteRepositoryFactory(Provider<NoteDao> daoProvider,
      Provider<NoteApi> apiProvider) {
    this.daoProvider = daoProvider;
    this.apiProvider = apiProvider;
  }

  @Override
  public NoteRepository get() {
    return provideNoteRepository(daoProvider.get(), apiProvider.get());
  }

  public static RepositoryModule_ProvideNoteRepositoryFactory create(
      javax.inject.Provider<NoteDao> daoProvider, javax.inject.Provider<NoteApi> apiProvider) {
    return new RepositoryModule_ProvideNoteRepositoryFactory(Providers.asDaggerProvider(daoProvider), Providers.asDaggerProvider(apiProvider));
  }

  public static RepositoryModule_ProvideNoteRepositoryFactory create(Provider<NoteDao> daoProvider,
      Provider<NoteApi> apiProvider) {
    return new RepositoryModule_ProvideNoteRepositoryFactory(daoProvider, apiProvider);
  }

  public static NoteRepository provideNoteRepository(NoteDao dao, NoteApi api) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideNoteRepository(dao, api));
  }
}
