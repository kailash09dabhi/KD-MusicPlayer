package com.kingbull.musicplayer.ui.main.categories.albumlist;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */
final class AlbumListCursorLoader extends CursorLoader {
  private static final Uri MEDIA_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
  private static final String ORDER_BY = MediaStore.Audio.Albums.ALBUM_KEY + " ASC";
  private static final String[] PROJECTIONS = {
      MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART,
  };

  public AlbumListCursorLoader(Context context) {
    super(context, MEDIA_URI, PROJECTIONS, null, null, ORDER_BY);
  }
}
