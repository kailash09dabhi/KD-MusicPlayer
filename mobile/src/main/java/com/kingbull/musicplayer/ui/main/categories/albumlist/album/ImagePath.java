package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import com.commit451.nativestackblur.NativeStackBlur;
import com.kingbull.musicplayer.R;

/**
 * @author Kailash Dabhi
 * @date 12/12/2016.
 */

public final class ImagePath {
  private final String path;

  public ImagePath(String path) {
    this.path = path;
  }

  Bitmap toBitmap(Resources resources) {
    Bitmap b = BitmapFactory.decodeFile(path);
    int wt_px = 300;
    if (b != null) {
      b = Bitmap.createScaledBitmap(b, (int) wt_px, (int) wt_px, true);
    } else {
      if (b == null) {
        b = BitmapFactory.decodeResource(resources, R.drawable.bg_default_album_art);
      }
      b = Bitmap.createScaledBitmap(b, (int) wt_px, (int) wt_px, true);
    }
    return b;
  }

  BitmapDrawable toBlurredBitmap(Bitmap bitmap, Resources resources) {
    bitmap = NativeStackBlur.process(bitmap, 68);
    BitmapDrawable d = new BitmapDrawable(resources, bitmap);
    ColorMatrix matrix = new ColorMatrix();
    matrix.setSaturation((float) 0.84);
    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
    d.setColorFilter(filter);
    return d;
  }
}
