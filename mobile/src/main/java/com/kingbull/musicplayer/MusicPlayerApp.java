package com.kingbull.musicplayer;

import android.app.Application;
import android.content.Intent;
import com.facebook.stetho.Stetho;
import com.kingbull.musicplayer.di.AppComponent;
import com.kingbull.musicplayer.di.AppModule;
import com.kingbull.musicplayer.di.DaggerAppComponent;
import com.kingbull.musicplayer.di.StorageModule;
import com.kingbull.musicplayer.player.MusicService;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class MusicPlayerApp extends Application {
  private static MusicPlayerApp application;
  private AppComponent appComponent;

  public static MusicPlayerApp instance() {
    return application;
  }

  public AppComponent component() {
    return application.appComponent;
  }

  @Override public void onCreate() {
    super.onCreate();
    application = this;
    // Custom fonts
    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath("fonts/JosefinSans-Regular.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build());
    injectDependencies();
    startService(new Intent(this, MusicService.class));
    Stetho.initializeWithDefaults(this);
  }

  private void injectDependencies() {
    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .storageModule(new StorageModule())
        .build();
  }
}
