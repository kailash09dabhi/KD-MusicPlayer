package com.kingbull.musicplayer.ui.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import com.kingbull.musicplayer.BuildConfig;
import com.kingbull.musicplayer.MusicPlayerApp;

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
    if (BuildConfig.DEBUG) {
      Configuration configuration = resources.getConfiguration();
      Log.e(TAG, "screenHeightDp " + configuration.screenHeightDp);
      Log.e(TAG, "screenWidthDp " + configuration.screenWidthDp);
      Log.e(TAG, "densityDpi " + resources.getDisplayMetrics().densityDpi);
      Log.e(TAG, "smallestScreenWidthDp " + configuration.smallestScreenWidthDp);
      String valueFolder =
          "values-sw" + configuration.smallestScreenWidthDp + "dp-" + densityBucket();
      Log.e(TAG, "If the folder exist it will pick this folder: --->  " + valueFolder);
      Toast.makeText(MusicPlayerApp.instance(), valueFolder, Toast.LENGTH_LONG).show();
    }
  }

  private String densityBucket() {
    String density = "";
    int densityDpi = resources.getDisplayMetrics().densityDpi;
    switch (densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        density = "ldpi";
        break;
      case DisplayMetrics.DENSITY_MEDIUM:
        density = "mdpi";
        break;
      case DisplayMetrics.DENSITY_HIGH:
        density = "hdpi";
        break;
      case DisplayMetrics.DENSITY_XHIGH:
        density = "xhdpi";
        break;
      case DisplayMetrics.DENSITY_XXHIGH:
        density = "xxhdpi";
        break;
      case DisplayMetrics.DENSITY_XXXHIGH:
        density = "xxxhdpi";
        break;
      case DisplayMetrics.DENSITY_TV:
        density = "tvdpi";
        break;
    }
    return density;
  }
}
