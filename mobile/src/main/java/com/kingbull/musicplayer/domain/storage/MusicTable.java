package com.kingbull.musicplayer.domain.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kingbull.musicplayer.domain.Music;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by Kailash Dabhi on 04-09-2016.
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
 */
public final class MusicTable implements SqlTable {
  public static final String NAME = "MusicTable";
  public static final String DEFINITION = "CREATE TABLE "
      + NAME
      + "("
      + Columns.SQLITE_ID
      + " INTEGER PRIMARY KEY,"
      + Columns.ID
      + " TEXT UNIQUE,"
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

  @Inject public MusicTable(SQLiteDatabase sqLiteDatabase) {
    this.sqliteDatabase = sqLiteDatabase;
  }

  public List<SqlMusic> fetchAll() {
    String query = "select * from " + MusicTable.NAME;
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<SqlMusic> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          SqlMusic item = new SqlMusic(new SqlMusicCursor(cursor));
          itemList.add(item);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  public List<Music> lastPlayedSongs() {
    String query = "select * from "
        + MusicTable.NAME
        + "  order  by datetime("
        + Columns.LAST_TIME_PLAYED
        + ") "
        + "DESC";
    ;
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<Music> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          SqlMusic song = new SqlMusic(new SqlMusicCursor(cursor));
          itemList.add(song);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  public List<Music> fetchAllWithMostRecentFirstOrder() {
    String query = "select * from "
        + MusicTable.NAME
        + "  order  by datetime("
        + Columns.UPDATED_AT
        + ") "
        + "DESC";
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<Music> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          Music item = new SqlMusic(new SqlMusicCursor(cursor));
          itemList.add(item);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  public Music fetchMostRecentTimestamp() {
    String query = "select * from "
        + MusicTable.NAME
        + "  order  by datetime("
        + Columns.CREATED_AT
        + ") DESC limit 1";
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    Music song = null;
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          song = new  SqlMusic(new SqlMusicCursor(cursor));
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return song;
  }

  @Override public void clear() {
    sqliteDatabase.delete(MusicTable.NAME, null, null);
  }

  public List<Music> mostPlayedSongs() {
    String query = "select * from "
        + MusicTable.NAME
        + "  order  by "
        + Columns.NUMBER_OF_TIMES_PLAYED
        + " DESC";
    ;
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<Music> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          SqlMusic song = new SqlMusic(new SqlMusicCursor(cursor));
          itemList.add(song);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  public static final class Columns {
    public static final String SQLITE_ID = "_id";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String PATH = "path";
    public static final String DATE_ADDED = "date_added";
    public static final String SIZE = "size";
    public static final String DURATION = "duration";
    public static final String FAVORITE = "favorite";
    public static final String LAST_TIME_PLAYED = "last_time_played";
    public static final String NUMBER_OF_TIMES_PLAYED = "number_of_times_played";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
  }
}
