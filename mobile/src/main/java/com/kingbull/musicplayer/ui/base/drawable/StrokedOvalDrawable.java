package com.kingbull.musicplayer.ui.base.drawable;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;

/**
 * @author Kailash Dabhi
 * @date 12/28/2016.
 */
final class StrokedOvalDrawable extends ShapeDrawable {
  public StrokedOvalDrawable(int color) {
    super(new OvalShape());
    Paint paint = getPaint();
    paint.setAntiAlias(true);
    paint.setColor(color);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(
        MusicPlayerApp.instance().getResources().getInteger(R.integer.oval_stroke_width));
  }
}
