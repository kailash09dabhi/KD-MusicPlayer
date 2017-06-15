package com.kingbull.musicplayer.ui.base.musiclist.ringtone;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import java.io.File;

/**
 * @author Kailash Dabhi
 * @date 12/11/2016.
 */
public final class Ringtone {
  public static final int PERMISSION_REQUEST_CODE = 999;
  private static Ringtone instance;
  private final Media media;

  public Ringtone(Media media) {
    this.media = media;
    instance = this;
  }

  public static void onPermissionGranted(Activity activity) {
    if (instance != null) instance.set(activity);
    instance = null;
  }

  private void set(Activity activity) {
    File ringtoneFile = new File(media.path());
    ContentValues content = new ContentValues();
    content.put(MediaStore.MediaColumns.DATA, ringtoneFile.getAbsolutePath());
    content.put(MediaStore.MediaColumns.TITLE, media.title());
    content.put(MediaStore.Audio.Media.ALBUM, media.album());
    content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
    content.put(MediaStore.Audio.Media.ARTIST, media.artist());
    content.put(MediaStore.Audio.Media.DURATION, media.duration());
    content.put(MediaStore.Audio.Media.IS_RINGTONE, true);
    content.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
    content.put(MediaStore.Audio.Media.IS_ALARM, false);
    content.put(MediaStore.Audio.Media.IS_MUSIC, false);
    Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
    activity.getContentResolver()
        .delete(uri, MediaStore.MediaColumns.DATA + "=\"" + ringtoneFile.getAbsolutePath() + "\"",
            null);
    Uri newUri = activity.getContentResolver().insert(uri, content);
    RingtoneManager.setActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_RINGTONE, newUri);
    new Snackbar(activity.findViewById(android.R.id.content)).show(
        media.title() + " is set as Ringtone successfully!");
  }

  public void requestPermissionToBeSet(Activity activity) {
    boolean permission;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      permission = Settings.System.canWrite(activity);
    } else {
      permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_SETTINGS)
          == PackageManager.PERMISSION_GRANTED;
    }
    if (permission) {
      //do your code
      set(activity);
    } else {
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, PERMISSION_REQUEST_CODE);
      } else {
        ActivityCompat.requestPermissions(activity,
            new String[] { Manifest.permission.WRITE_SETTINGS }, PERMISSION_REQUEST_CODE);
      }
    }
  }
}
