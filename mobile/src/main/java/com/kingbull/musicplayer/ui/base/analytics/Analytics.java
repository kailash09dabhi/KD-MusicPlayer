package com.kingbull.musicplayer.ui.base.analytics;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

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

  class Fabric implements Analytics {
    private final Answers analytics = Answers.getInstance();

    @Override public void logDurationFilter(int durationInSeconds) {
      analytics.logCustom(
          new CustomEvent("duration filter").putCustomAttribute("seconds", durationInSeconds));
    }

    @Override public void logBlurRadius(int radius) {
      analytics.logCustom(
          new CustomEvent("Background blurred").putCustomAttribute("blur radius", radius));
    }

    @Override public void logTheme(boolean isFlatTheme) {
      String theme = isFlatTheme ? "flat" : "glassy";
      analytics.logCustom(new CustomEvent("Theme changed").putCustomAttribute("to", theme));
    }

    @Override public void logFullScreen(boolean isFullScreen) {
      String screenMode = isFullScreen ? "full screen" : "normal screen";
      analytics.logCustom(
          new CustomEvent("screen mode").putCustomAttribute("screen mode", screenMode));
    }

    @Override public void logScreen(String name) {
      analytics.logCustom(new CustomEvent(name));
    }
  }
}
