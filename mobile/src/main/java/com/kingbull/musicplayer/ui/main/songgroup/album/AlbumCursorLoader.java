package com.kingbull.musicplayer.ui.main.songgroup.album;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class AlbumCursorLoader extends CursorLoader {
  private static final Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
  private static final String ORDER_BY = MediaStore.Audio.Media.ALBUM + " ASC";
  private static String[] PROJECTIONS = {
      MediaStore.Audio.Media.ALBUM_ID, // the real path
      MediaStore.Audio.Media.ALBUM,
  };

  public AlbumCursorLoader(Context context) {
    super(context, MEDIA_URI, PROJECTIONS, null, null, ORDER_BY);
  }
}
