package com.kingbull.musicplayer.ui.base.drawable;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/**
 * @author Kailash Dabhi
 * @date 4/4/2017
 */
final class FilledOvalDrawable extends ShapeDrawable {
  public FilledOvalDrawable(int color) {
    super(new OvalShape());
    Paint paint = getPaint();
    paint.setAntiAlias(true);
    paint.setColor(color);
    paint.setStyle(Paint.Style.FILL);
  }
}