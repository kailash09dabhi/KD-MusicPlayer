package com.kingbull.musicplayer.ui.base.analytics;

import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kingbull.musicplayer.MusicPlayerApp;

/**
 * @author Kailash Dabhi
 * @date 10/14/2016.
 */
public interface Analytics {
  void logDurationFilter();

  void logBlurRadius(int radius);

  void logTheme(boolean isFlatTheme);

  void logFullScreen(boolean isFullScreen);

  void logScreen(String name);

  class Firebase implements Analytics {
    private final FirebaseAnalytics firebaseAnalytics =
        FirebaseAnalytics.getInstance(MusicPlayerApp.instance());
    private final Bundle bundle = new Bundle();

    @Override public void logDurationFilter() {
      firebaseAnalytics.logEvent("duration_filter", bundle);
    }

    @Override public void logBlurRadius(int radius) {
      firebaseAnalytics.logEvent("blur_radius_" + radius, bundle);
    }

    @Override public void logTheme(boolean isFlatTheme) {
      firebaseAnalytics.logEvent("theme_" + (isFlatTheme ? "flat" : "glassy"), bundle);
    }

    @Override public void logFullScreen(boolean isFullScreen) {
      firebaseAnalytics.logEvent(isFullScreen ? "full_screen" : "normal_screen", bundle);
    }

    @Override public void logScreen(String name) {
      firebaseAnalytics.logEvent("screen_" + name, bundle);
    }
  }
}
