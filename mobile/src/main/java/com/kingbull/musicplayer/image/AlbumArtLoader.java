package com.kingbull.musicplayer.image;

import android.content.Context;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import java.io.InputStream;

final class AlbumArtLoader implements StreamModelLoader<AlbumArt> {
  @Override
  public DataFetcher<InputStream> getResourceFetcher(AlbumArt model, int width, int height) {
    return new AlbumArtFetcher(model);
  }

  static class Factory implements ModelLoaderFactory<AlbumArt, InputStream> {
    @Override public ModelLoader<AlbumArt, InputStream> build(Context context,
        GenericLoaderFactory factories) {
      return new AlbumArtLoader();
    }

    @Override public void teardown() {
    }
  }
}