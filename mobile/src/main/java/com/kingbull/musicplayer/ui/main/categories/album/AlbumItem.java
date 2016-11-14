package com.kingbull.musicplayer.ui.main.categories.album;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * @author Kailash Dabhi
 * @date 11/11/2016.
 */

public final class AlbumItem {
  private int _id;
  private String name;

  AlbumItem(Cursor cursor) {
    this._id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
    this.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
  }

  public int id() {
    return _id;
  }

  public String name() {
    return name;
  }
}