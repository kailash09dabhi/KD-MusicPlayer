package com.kingbull.musicplayer.domain.storage.sqlite.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.PlayList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 12/22/2016.
 */
public final class PlayListTable {
  private final String[] projections = new String[] {
      MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.NAME,
  };

  public PlayList addNewPlaylist(String name) {
    ContentValues values = new ContentValues();
    values.put(MediaStore.Audio.Playlists.NAME, name);
    values.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis());
    values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());
    Uri uri = MusicPlayerApp.instance()
        .getContentResolver()
        .insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values);
    PlayList playList = PlayList.NONE;
    if (uri != null) {
      Cursor cursor = MusicPlayerApp.instance()
          .getContentResolver()
          .query(uri, projections, null, null, MediaStore.Audio.Playlists.DATE_ADDED + " ASC");
      if (cursor != null) {
        if (cursor.getCount() > 0 && cursor.moveToLast()) {
          playList = new PlayList.Smart(cursor);
        }
        cursor.close();
      }
    }
    return playList;
  }

  public List<PlayList> allPlaylists() {
    Cursor cursor = MusicPlayerApp.instance()
        .getContentResolver()
        .query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, projections, null, null, null);
    List<PlayList> playLists = new ArrayList<>(1);
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          PlayList playList = new PlayList.Smart(cursor);
          playLists.add(playList);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    return playLists;
  }
}
