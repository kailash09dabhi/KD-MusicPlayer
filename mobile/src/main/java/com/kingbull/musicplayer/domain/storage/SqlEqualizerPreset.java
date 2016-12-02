package com.kingbull.musicplayer.domain.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import javax.inject.Inject;

public final class SqlEqualizerPreset implements EqualizerPreset, SqlTableRow {
  private final int y1;
  private final int y2;
  private final int y3;
  private final int y4;
  private final int y5;
  private final String name;
  @Inject SQLiteDatabase sqliteDatabase;

  public SqlEqualizerPreset(Cursor cursor) {
    y1 = cursor.getInt(cursor.getColumnIndexOrThrow(EqualizerPresetTable.Columns.Y1));
    y2 = cursor.getInt(cursor.getColumnIndexOrThrow(EqualizerPresetTable.Columns.Y2));
    y3 = cursor.getInt(cursor.getColumnIndexOrThrow(EqualizerPresetTable.Columns.Y3));
    y4 = cursor.getInt(cursor.getColumnIndexOrThrow(EqualizerPresetTable.Columns.Y4));
    y5 = cursor.getInt(cursor.getColumnIndexOrThrow(EqualizerPresetTable.Columns.Y5));
    name = cursor.getString(cursor.getColumnIndexOrThrow(EqualizerPresetTable.Columns.NAME));
    MusicPlayerApp.instance().component().inject(this);
  }

  public SqlEqualizerPreset(int y1, int y2, int y3, int y4, int y5, String name) {
    this.y1 = y1;
    this.y2 = y2;
    this.y3 = y3;
    this.y4 = y4;
    this.y5 = y5;
    this.name = name;
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public int y1Percentage() {
    return y1;
  }

  @Override public int y2Percentage() {
    return y2;
  }

  @Override public int y3Percentage() {
    return y3;
  }

  @Override public int y4Percentage() {
    return y4;
  }

  @Override public int y5Percentage() {
    return y5;
  }

  @Override public String name() {
    return name;
  }

  @Override public long save() {
    ContentValues values = new ContentValues();
    values.put(EqualizerPresetTable.Columns.Y1, y1);
    values.put(EqualizerPresetTable.Columns.Y2, y2);
    values.put(EqualizerPresetTable.Columns.Y3, y3);
    values.put(EqualizerPresetTable.Columns.Y4, y4);
    values.put(EqualizerPresetTable.Columns.Y5, y5);
    values.put(MediaStatTable.Columns.UPDATED_AT, new CurrentDateTime().toString());
    return sqliteDatabase.insertWithOnConflict(MediaStatTable.NAME, null, values,
        SQLiteDatabase.CONFLICT_REPLACE);
  }

  @Override public boolean delete() {
    return false;
  }
}