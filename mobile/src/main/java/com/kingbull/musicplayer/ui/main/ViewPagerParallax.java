package com.kingbull.musicplayer.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Debug;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import com.commit451.nativestackblur.NativeStackBlur;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.storage.preferences.PalettePreference;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeChangedEvent;
import com.kingbull.musicplayer.ui.base.UiColors;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import java.io.IOException;
import java.io.InputStream;

public final class ViewPagerParallax extends ViewPager {
  private final static String TAG = "ViewPagerParallax";
  int currentPosition = -1;
  float currentOffset = 0.0f;
  Window window;
  boolean isFlatTheme = false;
  private int backgroundId = -1;
  private int backgroundSavedId = -1;
  private int saved_width = -1;
  private int saved_height = -1;
  private int saved_max_num_pages = -1;
  private Bitmap savedBitmap;
  private boolean inSufficientMemory = false;
  private int maxNumPages = 5;
  private int imageHeight;
  private int imageWidth;
  private float zoomLevel;
  private float overlapLevel;
  private Rect src = new Rect(), dst = new Rect();
  private boolean pagingEnabled = true;
  private boolean parallaxEnabled = true;
  private boolean loggable = true;

  public ViewPagerParallax(Context context) {
    super(context);
    init();
  }

  public ViewPagerParallax(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    RxBus.getInstance()
        .toObservable()
        .ofType(ThemeChangedEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<ThemeChangedEvent>() {
          @Override public void accept(ThemeChangedEvent themeChangedEvent) throws Exception {
            isFlatTheme = new SettingPreferences().isFlatTheme();
            invalidate();
          }
        });
  }

  @SuppressLint("NewApi") private int sizeOf(Bitmap data) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
      return data.getRowBytes() * data.getHeight();
    } else {
      return data.getByteCount();
    }
  }

  private void setNewBackground() {
    if (backgroundId == -1) return;
    if (maxNumPages == 0) return;
    if (getWidth() == 0 || getHeight() == 0) return;
    if ((saved_height == getHeight()) && (saved_width == getWidth()) && (backgroundSavedId
        == backgroundId) && (saved_max_num_pages == maxNumPages)) {
      return;
    }
    if (savedBitmap != null && !savedBitmap.isRecycled()) savedBitmap.recycle();
    InputStream is;
    try {
      is = getContext().getResources().openRawResource(backgroundId);
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(is, null, options);
      imageHeight = options.outHeight;
      imageWidth = options.outWidth;
      if (loggable) Log.v(TAG, "imageHeight=" + imageHeight + ", imageWidth=" + imageWidth);
      zoomLevel = ((float) imageHeight) / getHeight();  // we are always in 'fitY' mode
      options.inJustDecodeBounds = false;
      options.inSampleSize = Math.round(zoomLevel);
      if (options.inSampleSize > 1) {
        imageHeight = imageHeight / options.inSampleSize;
        imageWidth = imageWidth / options.inSampleSize;
      }
      if (loggable) Log.v(TAG, "imageHeight=" + imageHeight + ", imageWidth=" + imageWidth);
      double max = Runtime.getRuntime().maxMemory(); //the maximum memory the app can use
      double heapSize = Runtime.getRuntime().totalMemory(); //current heap size
      double heapRemaining = Runtime.getRuntime().freeMemory(); //amount available in heap
      double nativeUsage = Debug.getNativeHeapAllocatedSize();
      double remaining = max - (heapSize - heapRemaining) - nativeUsage;
      int freeMemory = (int) (remaining / 1024);
      int bitmap_size = imageHeight * imageWidth * 4 / 1024;
      if (loggable) Log.v(TAG, "freeMemory = " + freeMemory);
      if (loggable) Log.v(TAG, "calculated bitmap size = " + bitmap_size);
      if (bitmap_size > freeMemory / 5) {
        inSufficientMemory = true;
        return; // we aren't going to use more than one fifth of free memory
      }
      zoomLevel = ((float) imageHeight) / getHeight();  // we are always in 'fitY' mode
      overlapLevel =
          zoomLevel * Math.min(Math.max(imageWidth / zoomLevel - getWidth(), 0) / (maxNumPages - 1),
              getWidth() / 2); // how many pixels to shift for each panel
      is.reset();
      Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
      savedBitmap = NativeStackBlur.process(bitmap, 45);
      bitmap.recycle();
      if (window != null) {
        Palette.from(savedBitmap).generate(new Palette.PaletteAsyncListener() {
          public void onGenerated(Palette palette) {
            if (palette != null) {
              new PalettePreference().save(palette);
              window.setBackgroundDrawable(new UiColors().statusBar().asDrawable());
              RxBus.getInstance().post(new PaletteEvent(palette));
            }
          }
        });
      }
      if (loggable) Log.i(TAG, "real bitmap size = " + sizeOf(savedBitmap) / 1024);
      if (loggable) {
        Log.v(TAG, "savedBitmap.getHeight()="
            + savedBitmap.getHeight()
            + ", savedBitmap.getWidth()="
            + savedBitmap.getWidth());
      }
      is.close();
    } catch (IOException e) {
      if (loggable) Log.e(TAG, "Cannot decode: " + e.getMessage());
      backgroundId = -1;
      return;
    }
    saved_height = getHeight();
    saved_width = getWidth();
    backgroundSavedId = backgroundId;
    saved_max_num_pages = maxNumPages;
  }

  @Override protected void onPageScrolled(int position, float offset, int offsetPixels) {
    super.onPageScrolled(position, offset, offsetPixels);
    currentPosition = position;
    currentOffset = offset;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (!inSufficientMemory
        && parallaxEnabled
        && savedBitmap != null
        && canvas != null
        && !isFlatTheme) {
      if (currentPosition == -1) currentPosition = getCurrentItem();
      // maybe we could get the current position from the getScrollX instead?
      src.set((int) (overlapLevel * (currentPosition + currentOffset)), 0,
          (int) (overlapLevel * (currentPosition + currentOffset) + (getWidth() * zoomLevel)),
          imageHeight);
      dst.set(getScrollX(), 0, getScrollX() + canvas.getWidth(), canvas.getHeight());
      canvas.drawBitmap(savedBitmap, src, dst, null);
    }
  }

  public void setMaxPages(int numMaxPages) {
    maxNumPages = numMaxPages;
    setNewBackground();
  }

  public void setBackgroundAsset(int resId, Window window) {
    backgroundId = resId;
    this.window = window;
    setNewBackground();
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if (!inSufficientMemory && parallaxEnabled) setNewBackground();
  }

  @Override public void setCurrentItem(int item) {
    super.setCurrentItem(item);
    currentPosition = item;
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (this.pagingEnabled) {
      return super.onTouchEvent(event);
    }
    return false;
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent event) {
    // fix for https://github.com/JakeWharton/Android-ViewPagerIndicator/issues/72
    if (isFakeDragging()) {
      return false;
    }
    if (this.pagingEnabled) {
      return super.onInterceptTouchEvent(event);
    }
    return false;
  }

  public boolean isPagingEnabled() {
    return pagingEnabled;
  }

  /**
   * Enables or disables paging for this ViewPagerParallax.
   */
  public void setPagingEnabled(boolean pagingEnabled) {
    this.pagingEnabled = pagingEnabled;
  }

  public boolean isParallaxEnabled() {
    return parallaxEnabled;
  }

  /**
   * Enables or disables parallax effect for this ViewPagerParallax.
   */
  public void setParallaxEnabled(boolean parallaxEnabled) {
    this.parallaxEnabled = parallaxEnabled;
  }

  protected void onDetachedFromWindow() {
    if (savedBitmap != null) {
      savedBitmap.recycle();
      savedBitmap = null;
    }
    super.onDetachedFromWindow();
  }
}