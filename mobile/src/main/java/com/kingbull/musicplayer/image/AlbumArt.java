package com.kingbull.musicplayer.image;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.Key;
import java.security.MessageDigest;

/**
 * @author Kailash Dabhi
 * @date 12/13/2016.
 */
public final class AlbumArt implements Key {
  private final String path;

  public AlbumArt(String path) {
    this.path = path;
  }

  public String path() {
    return path;
  }

  @Override public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
  }
}
