package com.kingbull.musicplayer;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatDelegate;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
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
    MobileAds.initialize(getApplicationContext(), "ca-app-pub-1642663068953785~5782766159");
    if (BuildConfig.DEBUG) {
      Stetho.initializeWithDefaults(this);
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
          .detectDiskWrites()
          .detectNetwork()   // or .detectAll() for all detectable problems
          .penaltyLog()
          .build());
      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
          .detectLeakedClosableObjects()
          .penaltyLog()
          .penaltyLog()
          .build());
    }
    // Custom fonts
    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.font_body))
            .setFontAttrId(R.attr.fontPath)
            .build());
    injectDependencies();
    startService(new Intent(this, MusicService.class));
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    AppEventsLogger.activateApp(this);
  }

  private void injectDependencies() {
    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .storageModule(new StorageModule())
        .build();
  }

  public String versionName() {
    String versionName = "";
    String packageName = getPackageName();
    try {
      versionName = getPackageManager().getPackageInfo(packageName, 0).versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return versionName;
  }
}
