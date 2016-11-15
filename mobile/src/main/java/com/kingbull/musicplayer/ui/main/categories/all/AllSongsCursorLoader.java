package com.kingbull.musicplayer.ui.main.categories.all;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class AllSongsCursorLoader extends CursorLoader {
  private static final Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
  private static String[] PROJECTIONS = {
      MediaStore.Audio.Media.DATA, // the real path
      MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.IS_RINGTONE, MediaStore.Audio.Media.IS_MUSIC,
      MediaStore.Audio.Media.IS_NOTIFICATION, MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST,
      MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
      MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.DATE_ADDED
  };

  public AllSongsCursorLoader(Context context) {
    super(context, MEDIA_URI, PROJECTIONS, null, null, null);
  }
}
