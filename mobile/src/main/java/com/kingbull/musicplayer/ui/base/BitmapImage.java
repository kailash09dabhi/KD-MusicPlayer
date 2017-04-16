package com.kingbull.musicplayer.ui.base;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import com.commit451.nativestackblur.NativeStackBlur;

/**
 * @author Kailash Dabhi
 * @date 4/16/2017
 */
public final class BitmapImage {
  private final Bitmap bitmap;
  private final Resources resources;

  public BitmapImage(Bitmap bitmap, Resources resources) {
    this.bitmap = bitmap;
    this.resources = resources;
  }

  public BitmapImage blurred() {
    return new BitmapImage(NativeStackBlur.process(this.bitmap, 68), resources);
  }

  public BitmapImage blurred(int radius) {
    return new BitmapImage(NativeStackBlur.process(this.bitmap, radius), resources);
  }

  public BitmapImage saturated() {
    ColorMatrix matrix = new ColorMatrix();
    matrix.setSaturation((float) 0.84);
    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
    BitmapDrawable bitmapDrawable = asBitmapDrawable();
    bitmapDrawable.setColorFilter(filter);
    return new BitmapImage(bitmapDrawable.getBitmap(), resources);
  }

  public BitmapDrawable asBitmapDrawable() {
    return new BitmapDrawable(resources, bitmap);
  }

  public Bitmap bitmap() {
    return bitmap;
  }
}
