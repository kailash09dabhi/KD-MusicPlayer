package com.kingbull.musicplayer.domain.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kingbull.musicplayer.domain.Song;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by Kailash Dabhi on 04-09-2016.
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
 */
public final class SongTable {
  public static final String NAME = "Songs";
  public static final String DEFINITION = "CREATE TABLE "
      + NAME
      + "("
      + Columns.SQLITE_ID
      + " INTEGER PRIMARY KEY,"
      + Columns.ID
      + " TEXT,"
      + Columns.TITLE
      + " TEXT,"
      + Columns.ALBUM
      + " TEXT,"
      + Columns.ARTIST
      + " TEXT,"
      + Columns.DATE_ADDED
      + " TEXT,"
      + Columns.DURATION
      + " TEXT,"
      + Columns.PATH
      + " TEXT,"
      + Columns.SIZE
      + " TEXT,"
      + Columns.FAVORITE
      + " TEXT,"
      + Columns.LAST_TIME_PLAYED
      + " TEXT,"
      + Columns.NUMBER_OF_TIMES_PLAYED
      + " TEXT,"
      + Columns.CREATED_AT
      + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
      + Columns.UPDATED_AT
      + " DATETIME"
      + ");";
  @Inject SQLiteDatabase sqliteDatabase;

  @Inject public SongTable(SQLiteDatabase sqLiteDatabase) {
    this.sqliteDatabase = sqLiteDatabase;
  }

  public List<Song> fetchAll() {
    String query = "select * from " + SongTable.NAME;
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<Song> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          Song item = new Song(cursor);
          itemList.add(item);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  public List<Song> fetchAllWithMostRecentFirstOrder() {
    String query =
        "select * from " + SongTable.NAME + "  order  by datetime(" + Columns.CREATED_AT + ") DESC";
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<Song> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          Song item = new Song(cursor);
          itemList.add(item);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  public Song fetchMostRecentTimestamp() {
    String query = "select * from "
        + SongTable.NAME
        + "  order  by datetime("
        + Columns.CREATED_AT
        + ") DESC limit 1";
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    Song song = null;
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          song = new Song(cursor);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return song;
  }

  public void deleteAll() {
    sqliteDatabase.delete(SongTable.NAME, null, null);
  }

  static final class Columns {
    static final String SQLITE_ID = "_id";
    static final String ID = "id";
    static final String TITLE = "title";
    static final String ARTIST = "artist";
    static final String ALBUM = "album";
    static final String PATH = "path";
    static final String DATE_ADDED = "date_added";
    static final String SIZE = "size";
    static final String DURATION = "duration";
    static final String FAVORITE = "favorite";
    static final String LAST_TIME_PLAYED = "last_time_played";
    static final String NUMBER_OF_TIMES_PLAYED = "number_of_times_played";
    static final String CREATED_AT = "created_at";
    static final String UPDATED_AT = "updated_at";
  }
}
