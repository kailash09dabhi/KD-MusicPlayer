package com.kingbull.musicplayer.domain.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import java.io.File;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */

public class SqlMusic implements com.kingbull.musicplayer.domain.Music, SqlTableRow {
  private final long duration;
  private final int size;
  @Inject SQLiteDatabase sqliteDatabase;
  private int id;
  private int sqlite_id;
  private String title;
  private String artist;
  private String album;
  //@Unique
  private String path;
  private long dateAdded;
  private boolean isFavorite;

  private long lastTimePlayed;
  private long numberOfTimesPlayed;

  public SqlMusic(Cursor cursor) {
    sqlite_id = cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.SQLITE_ID));
    id = cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.ID));
    title = cursor.getString(cursor.getColumnIndexOrThrow(MusicTable.Columns.TITLE));
    artist = cursor.getString(cursor.getColumnIndexOrThrow(MusicTable.Columns.ARTIST));
    album = cursor.getString(cursor.getColumnIndexOrThrow(MusicTable.Columns.ALBUM));
    path = cursor.getString(cursor.getColumnIndexOrThrow(MusicTable.Columns.PATH));
    duration = cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.DURATION));
    //dateAdded = (cursor.getLong(cursor.getColumnIndexOrThrow(
    //    MediaStore.Audio.Media.DATE_ADDED)));//this seems not working everytime gives 1970 date
    dateAdded = new File(path).lastModified();
    size = cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.DATE_ADDED));
    isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(MusicTable.Columns.FAVORITE)) == 1;
    MusicPlayerApp.instance().component().inject(this);
  }

  public SqlMusic(Music song) {
    id = song.id();
    title = song.title();
    artist = song.artist();
    album = song.album();
    path = song.path();
    duration = song.duration();
    //dateAdded = (cursor.getLong(cursor.getColumnIndexOrThrow(
    //    MediaStore.Audio.Media.DATE_ADDED)));//this seems not working everytime gives 1970 date
    dateAdded = new File(path).lastModified();
    size = song.size();
    isFavorite = song.isFavorite();
    lastTimePlayed = System.currentTimeMillis();
    numberOfTimesPlayed = numberOfTimesPlayed++;
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public int id() {
    return id;
  }

  @Override public String title() {
    return title;
  }

  @Override public String artist() {
    return artist;
  }

  @Override public String album() {
    return album;
  }

  @Override public String path() {
    return path;
  }

  @Override public long duration() {
    return duration;
  }

  @Override public int size() {
    return size;
  }

  @Override public long dateAdded() {
    return dateAdded;
  }

  @Override public boolean isFavorite() {
    return isFavorite;
  }

  @Override public long save() {
    ContentValues values = new ContentValues();
    values.put(MusicTable.Columns.ID, id);
    values.put(MusicTable.Columns.TITLE, title);
    values.put(MusicTable.Columns.ALBUM, album);
    values.put(MusicTable.Columns.ARTIST, artist);
    values.put(MusicTable.Columns.PATH, path);
    values.put(MusicTable.Columns.DATE_ADDED, dateAdded);
    values.put(MusicTable.Columns.DURATION, duration);
    values.put(MusicTable.Columns.LAST_TIME_PLAYED, lastTimePlayed);
    values.put(MusicTable.Columns.NUMBER_OF_TIMES_PLAYED, numberOfTimesPlayed);
    values.put(MusicTable.Columns.DATE_ADDED, dateAdded);
    values.put(MusicTable.Columns.UPDATED_AT, new CurrentDateTime().toString());
    return sqliteDatabase.insertWithOnConflict(MusicTable.NAME, null, values,
        SQLiteDatabase.CONFLICT_REPLACE);
  }

  @Override public boolean delete() {
    return sqliteDatabase.delete(MusicTable.NAME, MusicTable.Columns.SQLITE_ID + "=" + id, null)
        > 0;
  }

  @Override public String toString() {
    return "SqlMusic{" +
        "duration=" + duration +
        ", size=" + size +
        ", sqliteDatabase=" + sqliteDatabase +
        ", id=" + id +
        ", sqlite_id=" + sqlite_id +
        ", title='" + title + '\'' +
        ", artist='" + artist + '\'' +
        ", album='" + album + '\'' +
        ", path='" + path + '\'' +
        ", dateAdded=" + dateAdded +
        ", isFavorite=" + isFavorite +
        ", lastTimePlayed=" + lastTimePlayed +
        ", numberOfTimesPlayed=" + numberOfTimesPlayed +
        '}';
  }
}
