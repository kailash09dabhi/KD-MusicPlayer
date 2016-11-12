package com.kingbull.musicplayer;

import android.app.Application;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class MusicPlayerApp extends Application {
  private static MusicPlayerApp sInstance;

  public static MusicPlayerApp instance() {
    return sInstance;
  }

  @Override public void onCreate() {
    super.onCreate();
    sInstance = this;
    // Custom fonts
    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath("fonts/JosefinSans-Regular.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build());
  }
}
