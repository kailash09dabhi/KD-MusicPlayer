package com.kingbull.musicplayer;

import android.app.Application;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatDelegate;
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
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
          .detectDiskWrites()
          .detectNetwork()   // or .detectAll() for all detectable problems
          .penaltyLog()
          .build());
      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
          .detectLeakedClosableObjects()
          .penaltyLog()
          .penaltyDeath()
          .build());
    }
    // Custom fonts
    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.font_body))
            .setFontAttrId(R.attr.fontPath)
            .build());
    injectDependencies();
    startService(new Intent(this, MusicService.class));
    Stetho.initializeWithDefaults(this);
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  private void injectDependencies() {
    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .storageModule(new StorageModule())
        .build();
  }

  @Override public void onTerminate() {
    super.onTerminate();
    System.out.println("onTerminate");
  }

  @Override public void onLowMemory() {
    super.onLowMemory();
    System.out.println("onLowMemory");
  }

  @Override public void onTrimMemory(int level) {
    super.onTrimMemory(level);
    System.out.println("onTrimMemory");
  }
}
