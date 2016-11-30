package com.kingbull.musicplayer.domain.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * MusicListOfPlaylist by Kailash Dabhi on 04-09-2016.
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
 */
public final class MusicTable implements SqlTable {
  public static final String NAME = "MusicTable";
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

  @Inject public MusicTable(SQLiteDatabase sqLiteDatabase) {
    this.sqliteDatabase = sqLiteDatabase;
  }

  public List<Music> lastPlayedSongs() {
    String query = "select * from "
        + MusicTable.NAME
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
          SqlMusicCursor sqlMusicCursor = new SqlMusicCursor(cursor);
          Cursor mediaCursor = MusicPlayerApp.instance()
              .getContentResolver()
              .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] {
                  MediaStore.Audio.Media.DATA, // the real path
                  MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
                  MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST,
                  MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.IS_RINGTONE,
                  MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Media.IS_NOTIFICATION,
                  MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID,
                  MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED,
                  MediaStore.Audio.Media.YEAR
              }, MediaStore.Audio.Media._ID + " = ?", new String[] {
                  String.valueOf(sqlMusicCursor.mediaId())
              }, "");
          mediaCursor.moveToFirst();
          SqlMusic song = new SqlMusic(new SqlMusicCursor(cursor), new MediaCursor(mediaCursor));
          itemList.add(song);
          mediaCursor.close();
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }



  @Override public void clear() {
    sqliteDatabase.delete(MusicTable.NAME, null, null);
  }

  public List<Music> mostPlayedSongs() {
    String query = "select * from "
        + MusicTable.NAME
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
          SqlMusicCursor sqlMusicCursor = new SqlMusicCursor(cursor);
          Cursor mediaCursor = MusicPlayerApp.instance()
              .getContentResolver()
              .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] {
                  MediaStore.Audio.Media.DATA, // the real path
                  MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
                  MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST,
                  MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.IS_RINGTONE,
                  MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Media.IS_NOTIFICATION,
                  MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID,
                  MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED,
                  MediaStore.Audio.Media.YEAR
              }, MediaStore.Audio.Media._ID + " = ?", new String[] {
                  String.valueOf(sqlMusicCursor.mediaId())
              }, "");
          mediaCursor.moveToFirst();
          SqlMusic song = new SqlMusic(new SqlMusicCursor(cursor), new MediaCursor(mediaCursor));
          itemList.add(song);
          mediaCursor.close();
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  public List<Music> musicsOfPlayList(long playlistId) {
    String query =
        "select * from "
            + MusicTable.NAME
            + "  where "
            + Columns.PLAYLIST_IDS
            + " like "
            +
            "'%("
            + playlistId
            + ")%'"
            + " order by ("
            + MusicTable.Columns.UPDATED_AT
            + ") "
            + "DESC";
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<Music> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          SqlMusicCursor sqlMusicCursor = new SqlMusicCursor(cursor);
          Cursor mediaCursor = MusicPlayerApp.instance()
              .getContentResolver()
              .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] {
                  MediaStore.Audio.Media.DATA, // the real path
                  MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
                  MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST,
                  MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.IS_RINGTONE,
                  MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Media.IS_NOTIFICATION,
                  MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID,
                  MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED,
                  MediaStore.Audio.Media.YEAR
              }, MediaStore.Audio.Media._ID + " = ?", new String[] {
                  String.valueOf(sqlMusicCursor.mediaId())
              }, "");
          mediaCursor.moveToFirst();
          SqlMusic song = new SqlMusic(new SqlMusicCursor(cursor), new MediaCursor(mediaCursor));
          itemList.add(song);
          mediaCursor.close();
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return itemList;
  }

  public void addToPlaylist(List<SqlMusic> musicList, long playlistId) {
    for (SqlMusic music : musicList)
      music.addToPlayList(playlistId);
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
