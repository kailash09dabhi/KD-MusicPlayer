package com.kingbull.musicplayer.domain;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.ui.base.musiclist.AndroidMediaStoreDatabase;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 7/22/2017 8:24 PM
 */

public interface MusicGroup {
  List<Music> asList();

  interface Order {
    void by(@SortBy int sortBy);
  }

  class FromCursor implements MusicGroup {
    private final AndroidMediaStoreDatabase androidMediaStoreDatabase =
        new AndroidMediaStoreDatabase();
    private final Cursor cursor;

    public FromCursor(Cursor cursor) {
      this.cursor = cursor;
    }

    @Override public List<Music> asList() {
      List<Music> songs = new ArrayList<>();
      if (cursor != null && cursor.getCount() > 0) {
        cursor.moveToFirst();
        do {
          Music music = new SqlMusic(new Media.Smart(cursor));
          File file = new File(music.media().path());
          if (!file.exists()) {
            androidMediaStoreDatabase.deleteAndBroadcastDeletion(music.media().path());
          } else if (file.canRead()) {
            songs.add(music);
          }
        } while (cursor.moveToNext());
      }
      return songs;
    }
  }
}
