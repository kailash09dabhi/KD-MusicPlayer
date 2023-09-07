package com.kingbull.musicplayer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import androidx.multidex.MultiDexApplication;
import androidx.appcompat.app.AppCompatDelegate;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.internal.common.CrashlyticsReportWithSessionId;
import com.kingbull.musicplayer.di.AppComponent;
import com.kingbull.musicplayer.di.AppModule;
import com.kingbull.musicplayer.di.DaggerAppComponent;
import com.kingbull.musicplayer.di.StorageModule;
import com.kingbull.musicplayer.player.MusicService;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */
public final class MusicPlayerApp extends MultiDexApplication {
  private static MusicPlayerApp application;
  private AppComponent appComponent;

  public static MusicPlayerApp instance() {
    return application;
  }

  public AppComponent component() {
    return application.appComponent;
  }

  public FirebaseAnalytics firebaseAnalytics() {
    return FirebaseAnalytics.getInstance(this);
  }

  @Override public void onCreate() {
    super.onCreate();
    application = this;
    MobileAds.initialize(getApplicationContext(), initializationStatus -> {});
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
    ViewPump.init(ViewPump.builder()
        .addInterceptor(new CalligraphyInterceptor(
            new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_body))
                .setFontAttrId(R.attr.fontPath)
                .build()))
        .build());
    injectDependencies();
    startForegroundService(new Intent(this, MusicService.class));
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
