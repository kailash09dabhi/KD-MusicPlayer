package com.kingbull.musicplayer.ui.base;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.FloatRange;

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

  /**
   * Sets the darkness of the color to a value from 0 to 1
   *
   * @param darkness (should be between 1.0f - 0.0f)
   */
  public Color dark(@FloatRange(from = 0.0, to = 1.0) float darkness) {
    float[] hsv = new float[3];
    android.graphics.Color.colorToHSV(color, hsv);
    hsv[2] *= (1.0f - darkness); // value component
    return new Color(android.graphics.Color.HSVToColor(hsv));
  }

  public ColorDrawable ToSemiTransparentDrawable() {
    ColorDrawable colorDrawable = new ColorDrawable(color);
    colorDrawable.setAlpha(52);
    return colorDrawable;
  }

  /**
   * Sets the transparency of the color to a value from 0 to 1, where 0 means the color is
   * completely opaque and 1 means the color is completely transparent.
   */
  public Color transparent(@FloatRange(from = 0.0, to = 1.0)float factor) {
    if (factor >= 1.0) {
      return this;
    } else {
      return new Color(android.graphics.Color.argb(
          Math.round(android.graphics.Color.alpha(color) * (1.0f - factor)),
          android.graphics.Color.red(color), android.graphics.Color.green(color),
          android.graphics.Color.blue(color)));
    }
  }

  public int intValue() {
    return color;
  }

  public ColorDrawable asDrawable() {
    return new ColorDrawable(color);
  }

  public ColorDrawable toDrawable() {
    return new ColorDrawable(color);
  }
}
