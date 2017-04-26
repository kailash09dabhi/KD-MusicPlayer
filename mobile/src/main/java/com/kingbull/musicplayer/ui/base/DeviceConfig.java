package com.kingbull.musicplayer.ui.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @author Kailash Dabhi
 * @date 2/24/2017.
 */
public final class DeviceConfig {
  private static final String TAG = DeviceConfig.class.getName();
  private final Resources resources;

  public DeviceConfig(Resources resources) {
    this.resources = resources;
  }

  public void writeToLogcat() {
    Configuration configuration = resources.getConfiguration();
    Log.e(TAG, "screenWidthDp " + configuration.screenWidthDp);
    Log.e(TAG, "smallestScreenWidthDp " + configuration.smallestScreenWidthDp);
    Log.e(TAG, "screenHeightDp " + configuration.screenHeightDp);
    Log.e(TAG, "densityDpi " + resources.getDisplayMetrics().densityDpi);
    writeScreenDensityToLogcat();
  }

  private void writeScreenDensityToLogcat() {
    int densityDpi = resources.getDisplayMetrics().densityDpi;
    switch (densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        Log.e(TAG, "density ldpi");
        break;
      case DisplayMetrics.DENSITY_MEDIUM:
        Log.e(TAG, "density mdpi");
        break;
      case DisplayMetrics.DENSITY_HIGH:
        Log.e(TAG, "density hdpi");
        break;
      case DisplayMetrics.DENSITY_XHIGH:
        Log.e(TAG, "density xhdpi");
        break;
      case DisplayMetrics.DENSITY_XXHIGH:
        Log.e(TAG, "density xxhdpi");
        break;
      case DisplayMetrics.DENSITY_XXXHIGH:
        Log.e(TAG, "density xxxhdpi");
        break;
      case DisplayMetrics.DENSITY_TV:
        Log.e(TAG, "density tvdpi");
        break;
    }
  }
}
