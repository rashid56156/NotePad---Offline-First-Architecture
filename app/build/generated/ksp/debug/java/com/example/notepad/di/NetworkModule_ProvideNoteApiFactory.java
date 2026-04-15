package com.example.notepad.di;

import com.example.notepad.data.remote.api.NoteApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideNoteApiFactory implements Factory<NoteApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideNoteApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public NoteApi get() {
    return provideNoteApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideNoteApiFactory create(
      javax.inject.Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideNoteApiFactory(Providers.asDaggerProvider(retrofitProvider));
  }

  public static NetworkModule_ProvideNoteApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideNoteApiFactory(retrofitProvider);
  }

  public static NoteApi provideNoteApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideNoteApi(retrofit));
  }
}
