package com.kingbull.musicplayer.player;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * @author Kailash Dabhi
 * @date 14 June, 2017 7:18 PM
 */
public final class BitmapMemoryCache {
  private static volatile BitmapMemoryCache sInstance;
  private LruCache<String, Bitmap> cache;

  private BitmapMemoryCache() {
    // Get max available VM memory, exceeding this amount will throw an
    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
    // int in its constructor.
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;
    cache = new LruCache<String, Bitmap>(cacheSize) {
      @Override protected int sizeOf(String key, Bitmap bitmap) {
        // The cache size will be measured in kilobytes rather than
        // number of items.
        return bitmap.getByteCount() / 1024;
      }
    };
  }

  public static BitmapMemoryCache instance() {
    if (sInstance == null) {
      synchronized (Player.class) {
        if (sInstance == null) {
          sInstance = new BitmapMemoryCache();
        }
      }
    }
    return sInstance;
  }

  public void add(String key, Bitmap bitmap) {
    if (get(key) == null) {
      cache.put(key, bitmap);
    }
  }

  public Bitmap get(String key) {
    return cache.get(key);
  }
}
