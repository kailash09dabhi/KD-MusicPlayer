package com.kingbull.musicplayer.ui.main.categories.albumlist;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * @author Kailash Dabhi
 * @date 11/11/2016.
 */

public final class AlbumItem {
  private final int _id;
  private final String name;
  private final String albumArt;

  AlbumItem(Cursor cursor) {
    this._id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
    this.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
    this.albumArt =
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
  }

  public int id() {
    return _id;
  }

  public String name() {
    return name;
  }

  public String albumArt() {
    return albumArt;
  }
}