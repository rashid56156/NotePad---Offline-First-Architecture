package com.example.notepad.data.repository;

import com.example.notepad.data.local.dao.NoteDao;
import com.example.notepad.data.remote.api.NoteApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class NoteRepositoryImpl_Factory implements Factory<NoteRepositoryImpl> {
  private final Provider<NoteDao> daoProvider;

  private final Provider<NoteApi> apiProvider;

  public NoteRepositoryImpl_Factory(Provider<NoteDao> daoProvider, Provider<NoteApi> apiProvider) {
    this.daoProvider = daoProvider;
    this.apiProvider = apiProvider;
  }

  @Override
  public NoteRepositoryImpl get() {
    return newInstance(daoProvider.get(), apiProvider.get());
  }

  public static NoteRepositoryImpl_Factory create(javax.inject.Provider<NoteDao> daoProvider,
      javax.inject.Provider<NoteApi> apiProvider) {
    return new NoteRepositoryImpl_Factory(Providers.asDaggerProvider(daoProvider), Providers.asDaggerProvider(apiProvider));
  }

  public static NoteRepositoryImpl_Factory create(Provider<NoteDao> daoProvider,
      Provider<NoteApi> apiProvider) {
    return new NoteRepositoryImpl_Factory(daoProvider, apiProvider);
  }

  public static NoteRepositoryImpl newInstance(NoteDao dao, NoteApi api) {
    return new NoteRepositoryImpl(dao, api);
  }
}
