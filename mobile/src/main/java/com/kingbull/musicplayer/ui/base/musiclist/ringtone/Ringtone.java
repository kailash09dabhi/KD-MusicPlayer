package com.kingbull.musicplayer.ui.base.musiclist.ringtone;

import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import com.kingbull.musicplayer.domain.Music;
import java.io.File;

/**
 * @author Kailash Dabhi
 * @date 12/11/2016.
 */

public final class Ringtone {
  private static final int SET_RINGTONE_REQUEST_CODE = 999;
  private final Music music;
  private final Context context;

  public Ringtone(Context context, Music music) {
    this.context = context;
    this.music = music;
  }

  public void set() {
    File ringtoneFile = new File(music.media().path());
    ContentValues content = new ContentValues();
    content.put(MediaStore.MediaColumns.DATA, ringtoneFile.getAbsolutePath());
    content.put(MediaStore.MediaColumns.TITLE, music.media().title());
    content.put(MediaStore.Audio.Media.ALBUM, music.media().album());
    content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
    content.put(MediaStore.Audio.Media.ARTIST, music.media().artist());
    content.put(MediaStore.Audio.Media.DURATION, music.media().duration());
    content.put(MediaStore.Audio.Media.IS_RINGTONE, true);
    content.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
    content.put(MediaStore.Audio.Media.IS_ALARM, false);
    content.put(MediaStore.Audio.Media.IS_MUSIC, false);
    Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
    context.getContentResolver()
        .delete(uri, MediaStore.MediaColumns.DATA + "=\"" + ringtoneFile.getAbsolutePath() + "\"",
            null);
    Uri newUri = context.getContentResolver().insert(uri, content);
    RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
  }
}
