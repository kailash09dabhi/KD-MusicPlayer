package com.kingbull.musicplayer.domain.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import java.util.Date;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */

public final class SqlMusic
    implements com.kingbull.musicplayer.domain.Music, SqlTableRow, Parcelable {
  public static final Parcelable.Creator<SqlMusic> CREATOR = new Parcelable.Creator<SqlMusic>() {
    @Override public SqlMusic createFromParcel(Parcel source) {
      return new SqlMusic(source);
    }

    @Override public SqlMusic[] newArray(int size) {
      return new SqlMusic[size];
    }
  };
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
  private long numberOfTimesPlayed = 0;
  private String playlistId;// array of playlist id separated by ","

  public SqlMusic(MediaCursor cursor) {
    this((Music) cursor);
    SqlMusicCursor sqlMusicCursor = sqlMusicCursor(id);
    if (sqlMusicCursor != null) {
      isFavorite = sqlMusicCursor.isFavorite();
      lastTimePlayed = sqlMusicCursor.lastTimePlayed();
      numberOfTimesPlayed = sqlMusicCursor.numberOfTimesPlayed();
      playlistId = sqlMusicCursor.playlistIds();
      sqlMusicCursor.close();
    }
  }

  public SqlMusic(SqlMusicCursor cursor) {
    this((Music) cursor);
    isFavorite = cursor.isFavorite();
    lastTimePlayed = cursor.lastTimePlayed();
    numberOfTimesPlayed = cursor.numberOfTimesPlayed();
    playlistId = cursor.playlistIds();
    Log.e("lastTimePlayed", new Date(lastTimePlayed).toString());
    Log.e("NUMBER_OF_TIMES_PLAYED", String.valueOf(numberOfTimesPlayed));
  }

  public SqlMusic(Music music) {
    id = music.id();
    title = music.title();
    artist = music.artist();
    album = music.album();
    path = music.path();
    duration = music.duration();
    dateAdded = music.dateAdded();
    size = music.size();
    MusicPlayerApp.instance().component().inject(this);
  }

  public SqlMusic(Parcel in) {
    this.id = in.readInt();
    this.title = in.readString();
    this.artist = in.readString();
    this.album = in.readString();
    this.path = in.readString();
    this.duration = in.readLong();
    this.size = in.readInt();
    this.isFavorite = in.readInt() == 1;
    this.lastTimePlayed = in.readLong();
    this.numberOfTimesPlayed = in.readLong();
    MusicPlayerApp.instance().component().inject(this);
  }

  private SqlMusicCursor sqlMusicCursor(int mediaId) {
    Cursor cursor = sqliteDatabase.rawQuery("select "
        + MusicTable.Columns.FAVORITE
        + ", "
        + MusicTable.Columns.LAST_TIME_PLAYED
        + ", "
        + MusicTable.Columns.NUMBER_OF_TIMES_PLAYED
        + ", "
        + MusicTable.Columns.PLAYLIST_IDS
        + " from "
        + MusicTable.NAME
        + " where "
        + MusicTable.Columns.ID
        + " = ?", new String[] { String.valueOf(mediaId) });
    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToFirst();
      return new SqlMusicCursor(cursor);
    } else {
      return null;
    }
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

  @Override public long numberOfTimesPlayed() {
    return numberOfTimesPlayed;
  }

  @Override public long lastTimePlayed() {
    return lastTimePlayed;
  }

  @Override public long save() {
    ContentValues values = new ContentValues();
    values.put(MusicTable.Columns.ID, id);
    values.put(MusicTable.Columns.PLAYLIST_IDS, playlistId);
    values.put(MusicTable.Columns.TITLE, title);
    values.put(MusicTable.Columns.ALBUM, album);
    values.put(MusicTable.Columns.ARTIST, artist);
    values.put(MusicTable.Columns.PATH, path);
    values.put(MusicTable.Columns.DATE_ADDED, dateAdded);
    values.put(MusicTable.Columns.DURATION, duration);
    values.put(MusicTable.Columns.LAST_TIME_PLAYED, new CurrentDateTime().toString());
    values.put(MusicTable.Columns.NUMBER_OF_TIMES_PLAYED, numberOfTimesPlayed);
    values.put(MusicTable.Columns.DATE_ADDED, dateAdded);
    values.put(MusicTable.Columns.UPDATED_AT, new CurrentDateTime().toString());
    return sqliteDatabase.insertWithOnConflict(MusicTable.NAME, null, values,
        SQLiteDatabase.CONFLICT_REPLACE);
  }


  public void saveLastPlayed() {
    ContentValues values = new ContentValues();
    values.put(MusicTable.Columns.ID, id);
    values.put(MusicTable.Columns.TITLE, title);
    values.put(MusicTable.Columns.ALBUM, album);
    values.put(MusicTable.Columns.ARTIST, artist);
    values.put(MusicTable.Columns.PATH, path);
    values.put(MusicTable.Columns.DATE_ADDED, dateAdded);
    values.put(MusicTable.Columns.DURATION, duration);
    values.put(MusicTable.Columns.LAST_TIME_PLAYED, new CurrentDateTime().toString());
    values.put(MusicTable.Columns.NUMBER_OF_TIMES_PLAYED, ++numberOfTimesPlayed);
    values.put(MusicTable.Columns.DATE_ADDED, dateAdded);
    values.put(MusicTable.Columns.UPDATED_AT, new CurrentDateTime().toString());
    sqliteDatabase.insertWithOnConflict(MusicTable.NAME, null, values,
        SQLiteDatabase.CONFLICT_REPLACE);
  }

  @Override public boolean delete() {
    return sqliteDatabase.delete(MusicTable.NAME, MusicTable.Columns.ID + "=" + id, null) > 0;
  }

  public void addToPlayList(long playlistId) {
    this.playlistId = this.playlistId + "(" + playlistId + ")";
    save();
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

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.title);
    dest.writeString(this.artist);
    dest.writeString(this.album);
    dest.writeString(this.path);
    dest.writeLong(this.duration);
    dest.writeInt(this.size);
    dest.writeInt(this.isFavorite ? 1 : 0);
    dest.writeLong(this.lastTimePlayed);
    dest.writeLong(this.numberOfTimesPlayed);
  }
}
