package com.kingbull.musicplayer.ui.base.drawable;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.kingbull.musicplayer.MusicPlayerApp;

/**
 * @author Kailash Dabhi
 * @date 12/28/2016.
 */

public final class RoundLayerDrawable extends LayerDrawable {

  public RoundLayerDrawable(int drawableResource, int roundColor) {
    super(new Drawable[] {
        ContextCompat.getDrawable(MusicPlayerApp.instance(), drawableResource),
        new OvalShapeDrawable(roundColor)
    });
    int dp5 = dpToPx(5);
    setLayerInset(0, dp5, dp5, dp5, dp5);
  }

  private static int dpToPx(float dipValue) {
    DisplayMetrics metrics = MusicPlayerApp.instance().getResources().getDisplayMetrics();
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
  }
}
