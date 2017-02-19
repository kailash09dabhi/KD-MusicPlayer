package com.kingbull.musicplayer.ui.base;

import android.graphics.drawable.ColorDrawable;

/**
 * @author Kailash Dabhi
 * @date 12/30/2016.
 */

public final class Color {
  private final int color;

  public Color(int color) {
    this.color = color;
  }

  public Color light() {
    float[] hsv = new float[3];
    android.graphics.Color.colorToHSV(color, hsv);
    hsv[2] *= 1.25f; // value component
    return new Color(android.graphics.Color.HSVToColor(hsv));
  }

  public Color light(float value) {
    float[] hsv = new float[3];
    android.graphics.Color.colorToHSV(color, hsv);
    hsv[2] *= 1f + value; // value component
    return new Color(android.graphics.Color.HSVToColor(hsv));
  }

  public Color dark() {
    float[] hsv = new float[3];
    android.graphics.Color.colorToHSV(color, hsv);
    hsv[2] *= 0.8f; // value component
    return new Color(android.graphics.Color.HSVToColor(hsv));
  }

  public ColorDrawable ToSemiTransparentDrawable() {
    ColorDrawable colorDrawable = new ColorDrawable(color);
    colorDrawable.setAlpha(52);
    return colorDrawable;
  }

  public ColorDrawable toDrawable() {
    return new ColorDrawable(color);
  }
}
