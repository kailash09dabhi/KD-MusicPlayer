package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaTable;
import java.util.concurrent.TimeUnit;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */
final class AlbumSongsCursorLoader extends CursorLoader {
  private static final String ORDER_BY = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

  public AlbumSongsCursorLoader(Context context, long id) {
    super(context, MediaTable.URI, MediaTable.projections(),
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
