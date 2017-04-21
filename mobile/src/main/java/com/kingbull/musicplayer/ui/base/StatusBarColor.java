package com.kingbull.musicplayer.ui.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

/**
 * @author Kailash Dabhi
 * @date 4/8/2017
 */
public final class StatusBarColor {
  private static Interpolator fastOutSlowInInterpolator;
  private final int color;

  public StatusBarColor(Color color) {
    this.color = color.intValue();
  }

  public StatusBarColor(int color) {
    this.color = color;
  }

  public void applyOn(final Window window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //not needed if we set <item name="android:windowDrawsSystemBarBackgrounds">true</item> in style
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      ValueAnimator statusBarColorAnim = ValueAnimator.ofArgb(window.getStatusBarColor(), color);
      statusBarColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override public void onAnimationUpdate(ValueAnimator animation) {
          window.setStatusBarColor((int) animation.getAnimatedValue());
        }
      });
      statusBarColorAnim.setDuration(1000L);
      statusBarColorAnim.setInterpolator(fastOutSlowInInterpolator(window.getContext()));
      statusBarColorAnim.start();
    }
  }

  private static Interpolator fastOutSlowInInterpolator(Context context) {
    if (fastOutSlowInInterpolator == null) {
      fastOutSlowInInterpolator =
          AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in);
    }
    return fastOutSlowInInterpolator;
  }

  public int intValue() {
    return color;
  }
}
