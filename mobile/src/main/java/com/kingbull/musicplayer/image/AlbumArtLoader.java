package com.kingbull.musicplayer.image;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;

final class AlbumArtLoader implements ModelLoader<AlbumArt, InputStream> {

  @Nullable @Override
  public LoadData<InputStream> buildLoadData(@NonNull AlbumArt albumArt, int width, int height,
      @NonNull Options options) {
    return new LoadData<>(albumArt, new AlbumArtFetcher(albumArt));
  }

  @Override public boolean handles(@NonNull AlbumArt albumArt) {
    return true;
  }

  static class Factory implements ModelLoaderFactory<AlbumArt, InputStream> {

    @NonNull @Override
    public ModelLoader<AlbumArt, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
      return new AlbumArtLoader();
    }

    @Override public void teardown() {
    }
  }
}