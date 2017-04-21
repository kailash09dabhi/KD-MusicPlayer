package com.kingbull.musicplayer.domain.storage.sqlite.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlEqualizerPreset;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 12/23/2016.
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
      + Columns.NAME
      + " TEXT,"
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

  public List<EqualizerPreset> allPresets() {
    String query = "select * from "
        + EqualizerPresetTable.NAME
        + "  order  by datetime("
        + Columns.UPDATED_AT
        + ") "
        + "DESC";
    Cursor cursor = sqliteDatabase.rawQuery(query, null);
    List<EqualizerPreset> presets = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          SqlEqualizerPreset preset = new SqlEqualizerPreset(cursor);
          presets.add(preset);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return presets;
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
