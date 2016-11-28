package com.kingbull.musicplayer.domain.storage;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Music;
import java.util.List;

/**
 * Never ever use this class anywhere in app except as an argument to {@link SqlMusic} Class
 * consturctor. It is {@link Music} but it totally depends on cursor and it is just a medium to
 * build the {@link SqlMusic} Object.
 *
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */

public final class SqlPlaylistCursor implements PlayList {
  private final Cursor cursor;

  public SqlPlaylistCursor(Cursor cursor) {
    this.cursor = cursor;
  }

  public int id() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getInt(cursor.getColumnIndexOrThrow(PlayListTable.Columns.SQLITE_ID));
  }

  @Override public String name() {
    if (cursor.isClosed()) throw new IllegalStateException("cursor is closed!");
    return cursor.getString(cursor.getColumnIndexOrThrow(PlayListTable.Columns.NAME));
  }

  public void close() {
    cursor.close();
  }

  @Override public List<Music> musicList() {
    throw new UnsupportedOperationException("please use SqlPlayList object for this operation!");
  }
}
