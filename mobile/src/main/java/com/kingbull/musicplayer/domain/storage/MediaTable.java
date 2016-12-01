package com.kingbull.musicplayer.domain.storage;

import android.database.Cursor;
import android.provider.MediaStore;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Media;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

public final class MediaTable {

  public Media mediaById(long mediaId) {
    Cursor mediaCursor = MusicPlayerApp.instance()
        .getContentResolver()
        .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] {
            MediaStore.Audio.Media.DATA, // the real path
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_MUSIC, MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.YEAR
        }, MediaStore.Audio.Media._ID + " = ?", new String[] {
            String.valueOf(mediaId)
        }, "");
    mediaCursor.moveToFirst();
    Media media = null;
    if (mediaCursor != null && mediaCursor.getCount() > 0 && mediaCursor.moveToFirst()) {
      media = new Media.Smart(mediaCursor);
    } else {
      // TODO: 12/1/2016 null object pattern or something should be here 
      //media = new Media.Smart(mediaId);
    }
    mediaCursor.close();
    return media;
  }
}
