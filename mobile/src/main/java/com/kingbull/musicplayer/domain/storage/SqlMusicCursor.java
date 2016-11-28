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

  public int id() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.ID));
  }

  @Override public String title() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getString(cursor.getColumnIndexOrThrow(MusicTable.Columns.TITLE));
  }

  @Override public String artist() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getString(cursor.getColumnIndexOrThrow(MusicTable.Columns.ARTIST));
  }

  @Override public String album() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getString(cursor.getColumnIndexOrThrow(MusicTable.Columns.ALBUM));
  }

  @Override public String path() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getString(cursor.getColumnIndexOrThrow(MusicTable.Columns.PATH));
  }

  @Override public long duration() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.DURATION));
  }

  @Override public int size() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.SIZE));
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

  public void close() {
    cursor.close();
  }
}
