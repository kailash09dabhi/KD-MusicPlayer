package com.kingbull.musicplayer.di;

import android.content.res.Resources;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.player.MusicPlayer;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.analytics.Analytics;
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

  @Singleton @Provides Player provideMusicPlayer(SettingPreferences settingPreferences) {
    return new MusicPlayer(settingPreferences);
  }

  @Singleton @Provides @Named(SMART_THEME) ColorTheme provideSmartColorTheme(
      SettingPreferences settingPreferences) {
    return new ColorTheme.Smart(settingPreferences);
  }

  @Singleton @Provides @Named(FLAT_THEME) ColorTheme provideFlatColorTheme() {
    return new ColorTheme.Flat();
  }

  @Singleton @Provides Analytics provideAnalytics() {
    return new Analytics.Firebase();
  }
}