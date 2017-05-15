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

  class Firebase implements Analytics {
    private final FirebaseAnalytics firebaseAnalytics =
        FirebaseAnalytics.getInstance(MusicPlayerApp.instance());
    private final Bundle bundle = new Bundle();

    @Override public void logDurationFilter(int durationInSeconds) {
      firebaseAnalytics.logEvent("duration_filter", bundle);
      firebaseAnalytics.setUserProperty("duration_filter", String.valueOf(durationInSeconds));
    }

    @Override public void logBlurRadius(int radius) {
      firebaseAnalytics.logEvent("blur_radius_" + radius, bundle);
      firebaseAnalytics.setUserProperty("blur_radius", String.valueOf(radius));
    }

    @Override public void logTheme(boolean isFlatTheme) {
      String theme = isFlatTheme ? "flat" : "glassy";
      firebaseAnalytics.logEvent("theme_" + theme, bundle);
      firebaseAnalytics.setUserProperty("theme", theme);
    }

    @Override public void logFullScreen(boolean isFullScreen) {
      String screenMode = isFullScreen ? "full_screen" : "normal_screen";
      firebaseAnalytics.logEvent(screenMode, bundle);
      firebaseAnalytics.setUserProperty("screen_mode", screenMode);
    }

    @Override public void logScreen(String name) {
      firebaseAnalytics.logEvent("screen_" + name, bundle);
    }
  }
}
