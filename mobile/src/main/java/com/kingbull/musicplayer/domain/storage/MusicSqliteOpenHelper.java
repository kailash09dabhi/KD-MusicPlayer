package com.kingbull.musicplayer.domain.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class MusicSqliteOpenHelper extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "MusicTable.db";
  private static final int VERSION = 1;

  public MusicSqliteOpenHelper(Context context) {
    super(context, DATABASE_NAME, null /* factory */, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(MusicTable.DEFINITION);
    db.execSQL(PlayListTable.DEFINITION);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }
}
