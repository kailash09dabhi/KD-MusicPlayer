package com.kingbull.musicplayer.ui.base.analytics;

import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kingbull.musicplayer.MusicPlayerApp;

/**
 * @author Kailash Dabhi
 * @date 10/14/2016.
 */
public interface Analytics {
  void logDurationFilter(int durationInSeconds);

  void logBlurRadius(int radius);

  void logTheme(boolean isFlatTheme);

  void logFullScreen(boolean isFullScreen);

  void logScreen(String name);

  class Firebase implements Analytics{
    private final FirebaseAnalytics analytics = MusicPlayerApp.instance().firebaseAnalytics();

    @Override public void logDurationFilter(int durationInSeconds) {
      Bundle bundle = new Bundle();
      bundle.putInt("seconds", durationInSeconds);
      analytics.logEvent("duration_filter",bundle);
    }

    @Override public void logBlurRadius(int radius) {
      Bundle bundle = new Bundle();
      bundle.putInt("blur radius", radius);
      analytics.logEvent("background_blurred",bundle);
    }

    @Override public void logTheme(boolean isFlatTheme) {
      String theme = isFlatTheme ? "flat" : "glassy";
      Bundle bundle = new Bundle();
      bundle.putString("to", theme);
      analytics.logEvent("theme_changed",bundle);
    }

    @Override public void logFullScreen(boolean isFullScreen) {

      String screenMode = isFullScreen ? "full screen" : "normal screen";
      Bundle bundle = new Bundle();
      bundle.putString("screen_mode", screenMode);
      analytics.logEvent("screen_mode",bundle);
    }

    @Override public void logScreen(String name) {
      Bundle bundle = new Bundle();
      bundle.putString("screen_name", name);
      analytics.logEvent("screen_name",bundle);
    }
  }
}
