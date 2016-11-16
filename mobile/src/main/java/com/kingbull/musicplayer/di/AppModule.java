package com.kingbull.musicplayer.di;

import android.content.res.Resources;
import com.kingbull.musicplayer.MusicPlayerApp;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public final class AppModule {
  private MusicPlayerApp app;

  public AppModule(MusicPlayerApp app) {
    this.app = app;
  }

  @Singleton @Provides MusicPlayerApp provideApp() {
    return app;
  }

  @Singleton @Provides Resources provideResources() {
    return app.getResources();
  }
}