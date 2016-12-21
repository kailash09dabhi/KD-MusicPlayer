package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import java.util.concurrent.TimeUnit;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class AlbumSongsCursorLoader extends CursorLoader {
  private static final String ORDER_BY = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";
  private static String[] PROJECTIONS = {
      MediaStore.Audio.Media.DATA, // the real path
      MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.IS_RINGTONE,
      MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Media.IS_NOTIFICATION,
      MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.YEAR
  };

  public AlbumSongsCursorLoader(Context context,long id) {
    super(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, PROJECTIONS,
        MediaStore.Audio.Media.ALBUM_ID
            + "="
            + id
            + " AND "
            + MediaStore.Audio.Media.IS_MUSIC
            + "=1 AND "
            + MediaStore.Audio.Media.DURATION
            + " >= "
            + TimeUnit.SECONDS.toMillis(new SettingPreferences().filterDurationInSeconds()), null,
        ORDER_BY);
  }
}
