package com.kingbull.musicplayer.ui.base;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import com.commit451.nativestackblur.NativeStackBlur;

/**
 * Represents Image i.e. bitmap
 *
 * @author Kailash Dabhi
 * @date 19 July, 2017 8:01 PM
 */

public interface Image {
  Bitmap bitmap();

  class Base implements Image {
    protected final Image image;

    public Base(Image image) {
      this.image = image;
    }

    @Override public Bitmap bitmap() {
      return image.bitmap();
    }
  }

  class Saturated extends Base {
    public Saturated(Image image) {
      super(image);
    }

    @Override public Bitmap bitmap() {
      ColorMatrix matrix = new ColorMatrix();
      matrix.setSaturation((float) 0.84);
      ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
      BitmapDrawable bitmapDrawable = new BitmapDrawable(
          Resources.getSystem(), super.bitmap()
      );
      bitmapDrawable.setColorFilter(filter);
      return bitmapDrawable.getBitmap();
    }
  }

  class Blurred extends Base {
    private final int radius;

    public Blurred(Image image, int radius) {
      super(image);
      this.radius = radius;
    }

    public Blurred(Image image) {
      this(image, 68);
    }

    @Override public Bitmap bitmap() {
      return NativeStackBlur.process(super.bitmap(), radius);
    }
  }

  class Smart implements Image {
    private final Bitmap bitmap;

    public Smart(Bitmap bitmap) {
      this.bitmap = bitmap;
    }

    public Smart blurred() {
      return new Smart(
          new Image.Blurred(this).bitmap()
      );
    }

    public Smart blurred(int radius) {
      return new Smart(
          new Image.Blurred(this, radius).bitmap()
      );
    }

    public Smart saturated() {
      return new Smart(
          new Image.Saturated(this).bitmap()
      );
    }

    @Override public Bitmap bitmap() {
      return bitmap;
    }

    public BitmapDrawable bitmapDrawable() {
      return new BitmapDrawable(Resources.getSystem(), bitmap());
    }
  }
}
