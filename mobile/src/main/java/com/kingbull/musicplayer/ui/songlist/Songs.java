package com.kingbull.musicplayer.ui.songlist;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.MediaCursor;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class Songs {
  private Cursor cursor;

  Songs(Cursor cursor) {
    this.cursor = cursor;
  }

  Observable<List<Music>> toObservable() {
    List<Music> songs = new ArrayList<>();
    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToFirst();
      do {
        Music song = new SqlMusic(new MediaCursor(cursor));
        songs.add(song);
      } while (cursor.moveToNext());
    }
    return Observable.just(songs);
  }
}
