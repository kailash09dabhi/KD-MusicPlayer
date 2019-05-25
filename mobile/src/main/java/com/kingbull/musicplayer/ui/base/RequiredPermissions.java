package com.kingbull.musicplayer.ui.base;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @author Kailash Dabhi
 * @date 25 June, 2017 12:42 AM
 */
public final class RequiredPermissions {
  private final String[] group = {
      Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.READ_PHONE_STATE
  };
  private final Activity activity;

  public RequiredPermissions(Activity activity) {
    this.activity = activity;
  }

  public boolean isGranted() {
    return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
        == PackageManager.PERMISSION_GRANTED;
  }

  public void acquire(int requestCode) {
    ActivityCompat.requestPermissions(activity, group, requestCode);
  }
}


