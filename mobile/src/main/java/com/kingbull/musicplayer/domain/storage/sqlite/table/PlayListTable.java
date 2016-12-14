package com.kingbull.musicplayer.domain.storage.sqlite.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlPlayList;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * MusicListOfPlaylist by Kailash Dabhi on 04-09-2016.
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
 */
public final class PlayListTable implements SqlTable {
  public static final String NAME = "PlayListTable";
  public static final String DEFINITION = "CREATE TABLE "
      + NAME
      + "("
      + Columns.SQLITE_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
      + Columns.NAME
      + " TEXT UNIQUE,"
      + Columns.NUMBER_OF_TIMES_PLAYED
      + " TEXT,"
      + Columns.LAST_TIME_PLAYED
      + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
      + Columns.CREATED_AT
      + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
      + Columns.UPDATED_AT
      + " DATETIME"
      + ");";
  @Inject SQLiteDatabase sqliteDatabase;

  @Inject public PlayListTable(SQLiteDatabase sqLiteDatabase) {
    this.sqliteDatabase = sqLiteDatabase;
  }

  public List<PlayList> playlists() {
    String query = "select * from " + PlayListTable.NAME;
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<PlayList> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          PlayList item = new SqlPlayList(cursor);
          itemList.add(item);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  @Override public void clear() {
    sqliteDatabase.delete(PlayListTable.NAME, null, null);
  }

  public static final class Columns {
    public static final String SQLITE_ID = "_id";
    public static final String NAME = "name";
    public static final String LAST_TIME_PLAYED = "last_time_played";
    public static final String NUMBER_OF_TIMES_PLAYED = "number_of_times_played";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
  }
}
