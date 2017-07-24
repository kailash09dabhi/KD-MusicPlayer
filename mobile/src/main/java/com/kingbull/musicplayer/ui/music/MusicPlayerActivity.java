package com.kingbull.musicplayer.ui.music;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */
public final class MusicPlayerActivity extends BaseActivity<MusicPlayer.Presenter> {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      Window w = getWindow(); // in Activity's onCreate() for instance
      w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
          WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    super.onCreate(savedInstanceState);
    MusicPlayerApp.instance().component().inject(this);
    boolean hasSoftNavigationBar = hasSoftKeys();
    if (hasSoftNavigationBar) {
      int size = getSoftButtonsBarSizePort(this);
      Rect rect = new Rect(0, 0, 0, size);
      findViewById(android.R.id.content).setPadding(rect.left, rect.top, rect.right, rect.bottom);
    }
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(android.R.id.content, MusicPlayerFragment.newInstance(),
              MusicPlayerFragment.class.getSimpleName())
          .commit();
    }
  }

  private int getSoftButtonsBarSizePort(Activity activity) {
    // getRealMetrics is only available with API 17 and +
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      DisplayMetrics metrics = new DisplayMetrics();
      activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
      int usableHeight = metrics.heightPixels;
      activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
      int realHeight = metrics.heightPixels;
      if (realHeight > usableHeight) {
        return realHeight - usableHeight;
      } else {
        return 0;
      }
    }
    return 0;
  }

  private boolean hasSoftKeys() {
    boolean hasSoftwareKeys = true;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Display d = this.getWindowManager().getDefaultDisplay();
      DisplayMetrics realDisplayMetrics = new DisplayMetrics();
      d.getRealMetrics(realDisplayMetrics);
      int realHeight = realDisplayMetrics.heightPixels;
      int realWidth = realDisplayMetrics.widthPixels;
      DisplayMetrics displayMetrics = new DisplayMetrics();
      d.getMetrics(displayMetrics);
      int displayHeight = displayMetrics.heightPixels;
      int displayWidth = displayMetrics.widthPixels;
      hasSoftwareKeys = (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    } else {
      boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
      boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
      hasSoftwareKeys = !hasMenuKey && !hasBackKey;
    }
    return hasSoftwareKeys;
  }

  @NonNull @Override protected PresenterFactory<MusicPlayer.Presenter> presenterFactory() {
    return new PresenterFactory.MusicPlayer();
  }

  @Override protected void onPresenterPrepared(MusicPlayer.Presenter presenter) {
  }
}
