package com.kingbull.musicplayer.domain.storage.sqlite.table;

import android.database.Cursor;
import android.provider.MediaStore;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Album;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

public final class AlbumTable {
  private final String[] projections = new String[] {
      MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART,
  };

  public Album albumById(long id) {
    Cursor mediaCursor = MusicPlayerApp.instance()
        .getContentResolver()
        .query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projections,
            MediaStore.Audio.Media._ID + " = " + "?", new String[] {
                String.valueOf(id)
            }, "");
    mediaCursor.moveToFirst();
    Album album;
    if (mediaCursor != null && mediaCursor.getCount() > 0 && mediaCursor.moveToFirst()) {
      album = new Album.Smart(mediaCursor);
    } else {
      album = Album.NONE;
    }
    mediaCursor.close();
    return album;
  }
}
