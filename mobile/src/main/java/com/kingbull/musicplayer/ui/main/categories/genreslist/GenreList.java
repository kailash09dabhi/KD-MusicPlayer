package com.kingbull.musicplayer.ui.main.categories.genreslist;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
final class GenreList {
  private int _id;
  private String name;

  GenreList(Cursor cursor) {
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
