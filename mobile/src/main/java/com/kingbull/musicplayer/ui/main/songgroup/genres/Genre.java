package com.kingbull.musicplayer.ui.main.songgroup.genres;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class Genre {
  private int _id;
  private String name;

  Genre(Cursor cursor) {
    this._id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID));
    this.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME));
  }

  public int id() {
    return _id;
  }

  public String name() {
    return name;
  }
}
