package com.kingbull.musicplayer.ui.main.categories.genreslist.genre;

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

public final class GenreCursorLoader extends CursorLoader {
  private static final String ORDER_BY = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

  public GenreCursorLoader(Context context, int id) {
    super(context, MediaStore.Audio.Genres.Members.getContentUri("external", id),
        MediaTable.projections(),
        MediaStore.Audio.Media.DURATION + " >= " + TimeUnit.SECONDS.toMillis(
            new SettingPreferences().filterDurationInSeconds()), null, ORDER_BY);
  }
}
