package com.kingbull.musicplayer.domain.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 12/2/2016.
 */

public interface Equalizer {

  int y1();

  int y2();

  int y3();

  int y4();

  int y5();

  class Sql implements com.kingbull.musicplayer.domain.storage.Equalizer, SqlTableRow {
    private final int y1;
    private final int y2;
    private final int y3;
    private final int y4;
    private final int y5;
    @Inject SQLiteDatabase sqliteDatabase;

    @Inject public Sql(Cursor cursor) {
      y1 = cursor.getInt(cursor.getColumnIndexOrThrow(EqualizerTable.Columns.Y1));
      y2 = cursor.getInt(cursor.getColumnIndexOrThrow(EqualizerTable.Columns.Y2));
      y3 = cursor.getInt(cursor.getColumnIndexOrThrow(EqualizerTable.Columns.Y3));
      y4 = cursor.getInt(cursor.getColumnIndexOrThrow(EqualizerTable.Columns.Y4));
      y5 = cursor.getInt(cursor.getColumnIndexOrThrow(EqualizerTable.Columns.Y5));
    }

    @Inject public Sql(int y1, int y2, int y3, int y4, int y5) {
      this.y1 = y1;
      this.y2 = y2;
      this.y3 = y3;
      this.y4 = y4;
      this.y5 = y5;
    }

    @Override public int y1() {
      return y1;
    }

    @Override public int y2() {
      return y2;
    }

    @Override public int y3() {
      return y3;
    }

    @Override public int y4() {
      return y4;
    }

    @Override public int y5() {
      return y5;
    }

    @Override public long save() {
      ContentValues values = new ContentValues();
      values.put(EqualizerTable.Columns.Y1, y1);
      values.put(EqualizerTable.Columns.Y2, y2);
      values.put(EqualizerTable.Columns.Y3, y3);
      values.put(EqualizerTable.Columns.Y4, y4);
      values.put(EqualizerTable.Columns.Y5, y5);
      values.put(MediaStatTable.Columns.UPDATED_AT, new CurrentDateTime().toString());
      return sqliteDatabase.insertWithOnConflict(MediaStatTable.NAME, null, values,
          SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override public boolean delete() {
      return false;
    }
  }
}
