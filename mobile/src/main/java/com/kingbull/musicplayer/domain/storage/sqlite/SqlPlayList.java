package com.kingbull.musicplayer.domain.storage.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaStatTable;
import com.kingbull.musicplayer.domain.storage.sqlite.table.PlayListTable;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */

public final class SqlPlayList implements PlayList, SqlTableRow, Parcelable {
  public static final Creator<SqlPlayList> CREATOR = new Creator<SqlPlayList>() {
    @Override public SqlPlayList createFromParcel(Parcel source) {
      return new SqlPlayList(source);
    }

    @Override public SqlPlayList[] newArray(int size) {
      return new SqlPlayList[size];
    }
  };
  @Inject SQLiteDatabase sqliteDatabase;
  @Inject MediaStatTable mediaStatTable;
  private long createdAt;
  private long updatedAt;
  private String name;
  private long sqliteId = -1;
  private List<SqlMusic> musicList;

  public SqlPlayList(String name, List<SqlMusic> musicList) {
    this.name = name;
    this.musicList = musicList;
    MusicPlayerApp.instance().component().inject(this);
  }

  public SqlPlayList(Cursor cursor) {
    this.sqliteId =cursor.getInt(cursor.getColumnIndexOrThrow(PlayListTable.Columns.SQLITE_ID));
    this.name = cursor.getString(cursor.getColumnIndexOrThrow(PlayListTable.Columns.NAME));
    MusicPlayerApp.instance().component().inject(this);
  }

  public SqlPlayList(Parcel in) {
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public long save() {
    ContentValues values = new ContentValues();
    if (sqliteId != -1) values.put(PlayListTable.Columns.SQLITE_ID, sqliteId);
    values.put(PlayListTable.Columns.NAME, name);
    values.put(PlayListTable.Columns.CREATED_AT, createdAt);
    values.put(PlayListTable.Columns.UPDATED_AT, new CurrentDateTime().toString());
    sqliteId = sqliteDatabase.insertWithOnConflict(PlayListTable.NAME, null, values,
        SQLiteDatabase.CONFLICT_REPLACE);
    mediaStatTable.addToPlaylist(musicList, sqliteId);
    return sqliteId;
  }

  @Override public boolean delete() {
    return sqliteDatabase.delete(PlayListTable.NAME, PlayListTable.Columns.SQLITE_ID + "=" +
        sqliteId, null) > 0;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
  }

  @Override public String name() {
    return name;
  }

  @Override public List<Music> musicList() {
    return mediaStatTable.musicsOfPlayList(sqliteId);
  }

  public long id() {
    return sqliteId;
  }
}
