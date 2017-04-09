package com.kingbull.musicplayer.ui.base;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Kailash Dabhi
 * @date 4/8/2017
 */
public final class StatusBarColor {
  private final int color;

  public StatusBarColor(Color color) {
    this.color = color.intValue();
  }

  public StatusBarColor(int color) {
    this.color = color;
  }

  public void applyOn(Window window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //not needed if we set <item name="android:windowDrawsSystemBarBackgrounds">true</item> in style
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(color);
    }
  }

  public int intValue() {
    return color;
  }
}
