package com.kingbull.musicplayer.domain.storage.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.kingbull.musicplayer.domain.storage.sqlite.table.EqualizerPresetTable;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaStatTable;
import com.kingbull.musicplayer.domain.storage.sqlite.table.PlayListTable;

public final class MusicSqliteOpenHelper extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "Music.db";
  private static final int VERSION = 1;

  public MusicSqliteOpenHelper(Context context) {
    super(context, DATABASE_NAME, null /* factory */, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(MediaStatTable.DEFINITION);
    db.execSQL(PlayListTable.DEFINITION);
    db.execSQL(EqualizerPresetTable.DEFINITION);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }
}
