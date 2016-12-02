package com.kingbull.musicplayer.domain.storage;

import android.database.sqlite.SQLiteDatabase;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import javax.inject.Inject;

/**
 * MusicListOfPlaylist by Kailash Dabhi on 04-09-2016.
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
 */
public final class EqualizerPresetTable implements SqlTable {
  public static final String NAME = "EqualizerPresetTable";
  public static final String DEFINITION = "CREATE TABLE "
      + NAME
      + "("
      + Columns.SQLITE_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
      + Columns.Y1
      + " INTEGER,"
      + Columns.Y2
      + " INTEGER,"
      + Columns.Y3
      + " INTEGER,"
      + Columns.Y4
      + " INTEGER,"
      + Columns.Y5
      + " INTEGER,"
      + Columns.CREATED_AT
      + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
      + Columns.UPDATED_AT
      + " DATETIME"
      + ");";
  @Inject SQLiteDatabase sqliteDatabase;

  @Inject public EqualizerPresetTable(SQLiteDatabase sqLiteDatabase) {
    this.sqliteDatabase = sqLiteDatabase;
  }

  @Override public void clear() {
    sqliteDatabase.delete(EqualizerPresetTable.NAME, null, null);
  }

  public EqualizerPreset presetWith(int equalizerSqliteId) {
    //Cursor cursor = sqliteDatabase.rawQuery(
    //    "select * from " + EqualizerPresetTable.NAME + " where " + Columns.SQLITE_ID + " = ?",
    //    new String[] { String.valueOf(position) });
    //if (cursor != null && cursor.getCount() > 0) {
    //  cursor.moveToFirst();
    //  return new EqualizerPreset.Sql(cursor);
    //} else {
    //  return new EqualizerPreset.Sql(mediaId);
    //}
    return null;
  }

  public static final class Columns {
    public static final String SQLITE_ID = "_id";
    public static final String Y1 = "Y1";
    public static final String Y2 = "Y2";
    public static final String Y3 = "Y3";
    public static final String Y4 = "Y4";
    public static final String Y5 = "Y5";
    public static final String NAME = "name";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
  }
}
