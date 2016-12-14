package com.kingbull.musicplayer.domain.storage.sqlite.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.domain.MediaStat;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * MusicListOfPlaylist by Kailash Dabhi on 04-09-2016.
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
 */
public final class MediaStatTable implements SqlTable {
  public static final String NAME = "MediaStatTable";
  public static final String DEFINITION = "CREATE TABLE "
      + NAME
      + "("
      + Columns.SQLITE_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
      + Columns.MEDIA_ID
      + " INTEGER UNIQUE,"
      + Columns.PLAYLIST_IDS
      + " TEXT,"
      + Columns.FAVORITE
      + " TEXT,"
      + Columns.NUMBER_OF_TIMES_PLAYED
      + " INTEGER,"
      + Columns.LAST_TIME_PLAYED
      + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
      + Columns.CREATED_AT
      + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
      + Columns.UPDATED_AT
      + " DATETIME"
      + ");";
  @Inject SQLiteDatabase sqliteDatabase;

  @Inject public MediaStatTable(SQLiteDatabase sqLiteDatabase) {
    this.sqliteDatabase = sqLiteDatabase;
  }

  public List<Music> lastPlayedSongs() {
    String query = "select * from "
        + MediaStatTable.NAME
        + " where "
        + Columns.NUMBER_OF_TIMES_PLAYED
        + " > 0"
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
          SqlMusic song = new SqlMusic(new MediaStat.Smart(cursor));
          if (song.media() == Media.NONE) {
            song.mediaStat().delete();
          } else {
            itemList.add(song);
          }
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  @Override public void clear() {
    sqliteDatabase.delete(MediaStatTable.NAME, null, null);
  }

  public List<Music> mostPlayedSongs() {
    String query = "select * from "
        + MediaStatTable.NAME
        + " where "
        + Columns.NUMBER_OF_TIMES_PLAYED
        + " > 0"
        + " order  by "
        + Columns.NUMBER_OF_TIMES_PLAYED
        + " DESC";
    ;
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<Music> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          SqlMusic song = new SqlMusic(new MediaStat.Smart(cursor));
          if (song.media() == Media.NONE) {
            song.mediaStat().delete();
          } else {
            itemList.add(song);
          }
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  public List<Music> musicsOfPlayList(long playlistId) {
    String query = "select * from "
        + MediaStatTable.NAME
        + "  where "
        + Columns.PLAYLIST_IDS
        + " like "
        + "'%("
        + playlistId
        + ")%'"
        + " order by ("
        + MediaStatTable.Columns.UPDATED_AT
        + ") "
        + "DESC";
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<Music> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          SqlMusic song = new SqlMusic(new MediaStat.Smart(cursor));
          itemList.add(song);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  public void addToPlaylist(List<SqlMusic> musicList, long playlistId) {
    for (SqlMusic music : musicList)
      music.mediaStat().addToPlaylist(playlistId);
  }

  public MediaStat mediaStatById(long mediaId) {
    Cursor cursor = sqliteDatabase.rawQuery("select * from "
        + MediaStatTable.NAME
        + " where "
        + MediaStatTable.Columns.MEDIA_ID
        + " = ?", new String[] { String.valueOf(mediaId) });
    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToFirst();
      return new MediaStat.Smart(cursor);
    } else {
      return new MediaStat.Smart(mediaId);
    }
  }

  public static final class Columns {
    public static final String SQLITE_ID = "_id";
    public static final String MEDIA_ID = "media_id";

    public static final String FAVORITE = "favorite";
    public static final String PLAYLIST_IDS = "playlist_ids";// each song is appeneded with
    // playlist mediaId and playlist mediaId differentiated by "()"
    public static final String LAST_TIME_PLAYED = "last_time_played";
    public static final String NUMBER_OF_TIMES_PLAYED = "number_of_times_played";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
  }
}
