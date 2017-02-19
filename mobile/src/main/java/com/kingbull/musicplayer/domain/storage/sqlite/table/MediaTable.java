package com.kingbull.musicplayer.domain.storage.sqlite.table;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Media;
import java.io.File;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

public final class MediaTable {
  private final static String[] PROJECTIONS = new String[] {
      MediaStore.Audio.Media.DATA, // the real path
      MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.IS_RINGTONE,
      MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Media.IS_NOTIFICATION,
      MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.YEAR
  };
  public final static Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

  public final static String[] projections() {
    // Potential security hole! so lets fix it by clone it as per the Effective Java 2(Item 13)
    return PROJECTIONS.clone();
  }

  public Media mediaById(long mediaId) {
    Cursor mediaCursor = MusicPlayerApp.instance()
        .getContentResolver()
        .query(URI, PROJECTIONS, MediaStore.Audio.Media._ID + " = " + "?", new String[] {
            String.valueOf(mediaId)
        }, "");
    mediaCursor.moveToFirst();
    Media media;
    if (mediaCursor != null && mediaCursor.getCount() > 0 && mediaCursor.moveToFirst()) {
      media = new Media.Smart(mediaCursor);
    } else {
      media = Media.NONE;
    }
    mediaCursor.close();
    return media;
  }

  public Media mediaByFile(File file) {
    Cursor mediaCursor = MusicPlayerApp.instance()
        .getContentResolver()
        .query(URI, PROJECTIONS, MediaStore.Audio.Media.DATA + " = " + "?", new String[] {
            String.valueOf(file.getAbsolutePath())
        }, "");
    mediaCursor.moveToFirst();
    Media media;
    if (mediaCursor != null && mediaCursor.getCount() > 0 && mediaCursor.moveToFirst()) {
      media = new Media.Smart(mediaCursor);
    } else {
      media = new Media.Smart(file);
    }
    mediaCursor.close();
    return media;
  }
}
