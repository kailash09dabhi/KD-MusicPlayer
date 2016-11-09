package com.kingbull.musicplayer.ui.songlist;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class SongListCursorLoader extends CursorLoader {
  private static final String ORDER_BY = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";
  private static String[] PROJECTIONS = {
      MediaStore.Audio.Media.DATA, // the real path
      MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.IS_RINGTONE, MediaStore.Audio.Media.IS_MUSIC,
      MediaStore.Audio.Media.IS_NOTIFICATION, MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.SIZE
  };

  public SongListCursorLoader(Context context,int id) {
    super(context, MediaStore.Audio.Genres.Members.getContentUri("external", id), PROJECTIONS, null,
        null, ORDER_BY);
  }
}
