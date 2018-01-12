package com.kingbull.musicplayer.image;

import android.content.ComponentCallbacks2;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.kingbull.musicplayer.MusicPlayerApp;

/**
 * Bitmap pool which handle eviction policy automatically.
 *
 * @author Kailash Dabhi
 * @date 7/20/2017 11:03 PM
 */

public class GlideBitmapPool {
  private static volatile GlideBitmapPool instance;
  private final BitmapPool bitmapPool;

  private GlideBitmapPool() {
    bitmapPool = new LruBitmapPool(
        new MemorySizeCalculator.Builder(
            MusicPlayerApp.instance()
        ).build().getBitmapPoolSize()
    );
  }

  public static GlideBitmapPool instance() {
    if (instance == null) {
      synchronized (GlideBitmapPool.class) {
        if (instance == null) {
          instance = new GlideBitmapPool();
        }
      }
    }
    return instance;
  }

  public void put(Bitmap bitmap) {
    bitmapPool.put(bitmap);
  }

  public void clear() {
    bitmapPool.clearMemory();
    bitmapPool.trimMemory(ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL);
  }
}
