package com.kingbull.musicplayer.ui.main.categories.all;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaTable;
import java.util.concurrent.TimeUnit;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class AllSongsCursorLoader extends CursorLoader {
  private static final Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

  public AllSongsCursorLoader(Context context) {
    super(context, MEDIA_URI, MediaTable.projections(), MediaStore.Audio.Media.DURATION + " >= ?",
        new String[] {
            String.valueOf(
                TimeUnit.SECONDS.toMillis(new SettingPreferences().filterDurationInSeconds()))
        }, null);
  }
}
