package com.kingbull.musicplayer.ui.main.categories.genreslist;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.loader.content.CursorLoader;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */
final class GenresListCursorLoader extends CursorLoader {
  private static final Uri MEDIA_URI = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
  private static final String ORDER_BY = MediaStore.Audio.Genres.NAME + " ASC";
  private static final String[] PROJECTIONS = {
      MediaStore.Audio.Genres._ID, // the real path
      MediaStore.Audio.Genres.NAME,
  };

  public GenresListCursorLoader(Context context) {
    super(context, MEDIA_URI, PROJECTIONS, null, null, ORDER_BY);
  }
}
