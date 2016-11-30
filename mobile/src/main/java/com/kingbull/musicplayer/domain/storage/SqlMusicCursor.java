package com.kingbull.musicplayer.domain.storage;

import android.database.Cursor;
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

public final class SqlMusicCursor implements Music {
  private final Cursor cursor;

  public SqlMusicCursor(Cursor cursor) {
    this.cursor = cursor;
  }

  public int sqliteId() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.SQLITE_ID));
  }

  @Override public int mediaId() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.MEDIA_ID));
  }

  @Override public String title() {
    throw new UnsupportedOperationException("title() is MediaCursor's method!");
  }

  @Override public String artist() {
    throw new UnsupportedOperationException("artist() is MediaCursor's method!");
  }

  @Override public String album() {
    throw new UnsupportedOperationException("album() is MediaCursor's method!");
  }

  @Override public String path() {
    throw new UnsupportedOperationException("path() is MediaCursor's method!");
  }

  @Override public long duration() {
    throw new UnsupportedOperationException("duration() is MediaCursor's method!");
  }

  @Override public int size() {
    throw new UnsupportedOperationException("size() is MediaCursor's method!");
  }

  @Override public long dateAdded() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    //dateAdded = (cursor.getLong(cursor.getColumnIndexOrThrow(
    //    MediaStore.Audio.Media.DATE_ADDED)));//this seems not working everytime gives 1970 date
    return new File(path()).lastModified();
  }

  @Override public boolean isFavorite() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.FAVORITE)) == 1;
  }

  @Override public long numberOfTimesPlayed() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getLong(cursor.getColumnIndexOrThrow(MusicTable.Columns.NUMBER_OF_TIMES_PLAYED));
  }

  public String playlistIds() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getString(cursor.getColumnIndexOrThrow(MusicTable.Columns.PLAYLIST_IDS));
  }

  @Override public long lastTimePlayed() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getLong(cursor.getColumnIndexOrThrow(MusicTable.Columns.LAST_TIME_PLAYED));
  }

  @Override public long year() {
    throw new UnsupportedOperationException("year() is MediaCursor's method!");
  }

  public void close() {
    cursor.close();
  }
}
