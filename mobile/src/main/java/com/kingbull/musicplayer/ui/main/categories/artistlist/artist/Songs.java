package com.kingbull.musicplayer.ui.main.categories.artistlist.artist;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class Songs {
  private Cursor cursor;

  Songs(Cursor cursor) {
    this.cursor = cursor;
  }

  Flowable<List<Music>> toFlowable() {
    List<Music> songs = new ArrayList<>();
    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToFirst();
      do {
        Music song = new SqlMusic(new Media.Smart(cursor));
        songs.add(song);
      } while (cursor.moveToNext());
    }
    return Flowable.just(songs);
  }
}
