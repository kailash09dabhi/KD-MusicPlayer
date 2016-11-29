package com.kingbull.musicplayer.domain.storage;

import android.database.Cursor;
import android.provider.MediaStore;
import com.kingbull.musicplayer.domain.Music;
import java.io.File;

/**
 * Never ever use this class anywhere in app except as an argument to {@link SqlMusic} Class
 * consturctor. It is {@link Music} but it totally depends on cursor and it is just a medium to
 * build the {@link SqlMusic} Object.
 *
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */

public final class MediaCursor implements Music {
  private final Cursor cursor;

  public MediaCursor(Cursor cursor) {
    this.cursor = cursor;
  }

  public int mediaId() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
  }

  @Override public String title() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
  }

  @Override public String artist() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
  }

  @Override public String album() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
  }

  @Override public String path() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
  }

  @Override public long duration() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
  }

  @Override public int size() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
  }

  @Override public long dateAdded() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return new File(path()).lastModified();
  }

  @Override public boolean isFavorite() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return false;
  }

  @Override public long numberOfTimesPlayed() {
    throw new NoSuchFieldError("MediaCursor does not have the column number_of_times_played!");
  }

  @Override public long lastTimePlayed() {
    throw new NoSuchFieldError("MediaCursor does not have the column last_time_played!");
  }
}
