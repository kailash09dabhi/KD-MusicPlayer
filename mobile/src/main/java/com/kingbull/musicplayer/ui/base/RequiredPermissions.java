package com.kingbull.musicplayer.ui.base;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @author Kailash Dabhi
 * @date 25 June, 2017 12:42 AM
 */
public final class RequiredPermissions {
  private  String[] group = {
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.READ_PHONE_STATE
  };
  private final Activity activity;

  public RequiredPermissions(Activity activity) {
    this.activity = activity;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      this.group = append(group,Manifest.permission.READ_MEDIA_AUDIO);
    }
  }
  private static <T> T[] append(T[] arr, T lastElement) {
    final int N = arr.length;
    arr = java.util.Arrays.copyOf(arr, N+1);
    arr[N] = lastElement;
    return arr;
  }
  public boolean isGranted() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_AUDIO)
          == PackageManager.PERMISSION_GRANTED
          && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
          == PackageManager.PERMISSION_GRANTED;
    }else
      return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
        == PackageManager.PERMISSION_GRANTED;
  }

  public void acquire(int requestCode) {
    ActivityCompat.requestPermissions(activity, group, requestCode);
  }
}


