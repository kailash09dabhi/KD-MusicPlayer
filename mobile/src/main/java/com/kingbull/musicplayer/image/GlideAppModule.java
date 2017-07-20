package com.kingbull.musicplayer.image;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;
import java.io.InputStream;

public final class GlideAppModule implements GlideModule {
  @Override public void applyOptions(Context context, GlideBuilder builder) {
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;
    builder.setMemoryCache(new LruResourceCache(cacheSize));
  }

  @Override public void registerComponents(Context context, Glide glide) {
    glide.register(AlbumArt.class, InputStream.class, new AlbumArtLoader.Factory());
  }
}