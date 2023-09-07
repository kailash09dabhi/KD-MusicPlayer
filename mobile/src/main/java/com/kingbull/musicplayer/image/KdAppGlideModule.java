package com.kingbull.musicplayer.image;

import android.content.Context;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import java.io.InputStream;

/**
 * Represents AppGlideModule to let the Glide annotation processor generate code for glide builders.
 */
@GlideModule
public final class KdAppGlideModule extends AppGlideModule {

  @Override public void applyOptions(Context context, GlideBuilder builder) {
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;
    builder.setMemoryCache(new LruResourceCache(cacheSize));
  }

  @Override public void registerComponents(@NonNull Context context, @NonNull Glide glide,
      @NonNull Registry registry) {
    registry.append(AlbumArt.class, InputStream.class, new AlbumArtLoader.Factory());
  }
}