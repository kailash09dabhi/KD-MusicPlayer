package com.kingbull.musicplayer.ui.main.categories.playlists.recentlyadded;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class RecentlyAddedCursorLoader extends CursorLoader {
  private static final Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
  private static final String[] PROJECTIONS = {
      MediaStore.Audio.Media.DATA, // the real path
      MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.IS_RINGTONE, MediaStore.Audio.Media.IS_MUSIC,
      MediaStore.Audio.Media.IS_NOTIFICATION, MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST,
      MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATE_ADDED,
      MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION
  };
  // do a query for all songs added in the last 52 weeks
private final static   int X = 52 * (3600 * 24 * 7);
  private static final String WHERE =
      MediaStore.MediaColumns.DATE_ADDED + ">" + (System.currentTimeMillis() / 1000 - X);
  final String[] ccols = new String[] { MediaStore.Audio.Media._ID };

  public RecentlyAddedCursorLoader(Context context) {
    super(context, MEDIA_URI, PROJECTIONS, WHERE, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
  }
}
