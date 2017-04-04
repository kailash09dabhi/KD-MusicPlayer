package com.kingbull.musicplayer.ui.base.drawable;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.kingbull.musicplayer.MusicPlayerApp;

/**
 * @author Kailash Dabhi
 * @date 4/4/2017
 */
public final class IconDrawable extends LayerDrawable {
  public IconDrawable(int drawableResource, int strokeColor, int fillColor) {
    super(new Drawable[] {
        new FilledOvalDrawable(fillColor),
        ContextCompat.getDrawable(MusicPlayerApp.instance(), drawableResource),
        new StrokedOvalDrawable(strokeColor),
    });
    int dp5 = dpToPx(8);
    setLayerInset(1, dp5, dp5, dp5, dp5);
  }

  private static int dpToPx(float dipValue) {
    DisplayMetrics metrics = MusicPlayerApp.instance().getResources().getDisplayMetrics();
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
  }
}
