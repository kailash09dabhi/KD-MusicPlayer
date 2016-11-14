package com.kingbull.musicplayer.ui.songlist;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Song;
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

  Observable<List<Song>> toObservable() {
    List<Song> songs = new ArrayList<>();
    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToFirst();
      do {
        Song song = new Song(cursor);
        songs.add(song);
      } while (cursor.moveToNext());
    }
    return Observable.just(songs);
  }
}
