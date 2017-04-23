package com.kingbull.musicplayer.di;

import android.content.res.Resources;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.player.MusicPlayer;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module public final class AppModule {
  public static final String SMART_THEME = "smart_theme";
  public static final String FLAT_THEME = "flat_theme";
  private final MusicPlayerApp app;

  public AppModule(MusicPlayerApp app) {
    this.app = app;
  }

  @Singleton @Provides MusicPlayerApp provideApp() {
    return app;
  }

  @Singleton @Provides Resources provideResources() {
    return app.getResources();
  }

  @Singleton @Provides Player provideMusicPlayer() {
    return new MusicPlayer();
  }

  @Singleton @Provides @Named(SMART_THEME) ColorTheme provideSmartColorTheme() {
    return new ColorTheme.Smart();
  }

  @Singleton @Provides @Named(FLAT_THEME) ColorTheme provideFlatColorTheme() {
    return new ColorTheme.Flat();
  }
}