package com.kingbull.musicplayer.image;

import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.crashlytics.android.Crashlytics;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

final class AlbumArtFetcher implements DataFetcher<InputStream> {

  private static final String[] FALLBACKS = {"cover.jpg", "album.jpg", "folder.jpg"};
  private final AlbumArt model;
  private FileInputStream stream;

  public AlbumArtFetcher(AlbumArt model) {
    this.model = model;
  }

  @Override public void loadData(@NonNull Priority priority,
      @NonNull DataCallback<? super InputStream> callback) {
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    try {
      retriever.setDataSource(model.path());
      byte[] picture = retriever.getEmbeddedPicture();
      if (picture != null) {
        callback.onDataReady(new ByteArrayInputStream(picture));
      } else {
        InputStream inputStream = fallback(model.path());
        if (inputStream == null) {
          callback.onLoadFailed(new Exception("No picture found!"));
        }
      }
    } finally {
      retriever.release();
    }
  }

  private InputStream fallback(String path) {
    File parent = new File(path).getParentFile();
    for (String fallback : FALLBACKS) {
      // TODO make it smarter by enumerating folder contents and filtering for files
      // example algorithm for that: http://askubuntu.com/questions/123612/how-do-i-set-album-artwork
      File cover = new File(parent, fallback);
      if (cover.exists()) {
        try {
          return stream = new FileInputStream(cover);
        } catch (FileNotFoundException e) {
          Crashlytics.logException(e);
        }
      }
    }
    return null;
  }

  @Override public void cleanup() {
    // already cleaned up in loadData and ByteArrayInputStream will be GC'd
    if (stream != null) {
      try {
        stream.close();
      } catch (IOException ignore) {
        // can't do much about it
      }
    }
  }

  @Override public void cancel() {
    // cannot cancel
  }

  @NonNull @Override public Class<InputStream> getDataClass() {
    return InputStream.class;
  }

  @NonNull @Override public DataSource getDataSource() {
    return DataSource.LOCAL;
  }
}