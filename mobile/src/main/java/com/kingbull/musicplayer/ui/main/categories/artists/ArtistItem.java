package com.kingbull.musicplayer.ui.main.categories.artists;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * @author Kailash Dabhi
 * @date 11/11/2016.
 */

public final class ArtistItem {
  private int _id;
  private String name;

  ArtistItem(Cursor cursor) {
    this._id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
    this.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
  }

  public int id() {
    return _id;
  }

  public String name() {
    return name;
  }
}