package com.kingbull.musicplayer.domain.storage.sqlite.table;

import android.database.Cursor;
import android.provider.MediaStore;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.PlayList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 12/22/2016.
 */

public final class AndroidPlayListTable {
  private final String[] projections = new String[] {
      MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.NAME,
  };

  public List<PlayList> allPlaylists() {
    Cursor cursor = MusicPlayerApp.instance()
        .getContentResolver()
        .query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, projections, null, null, null);
    List<PlayList> playLists = new ArrayList<>(1);
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          PlayList playList = new PlayList.Smart(cursor);
          playLists.add(playList);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return playLists;
  }
}
