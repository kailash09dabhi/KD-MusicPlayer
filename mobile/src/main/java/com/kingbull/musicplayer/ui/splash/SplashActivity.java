package com.kingbull.musicplayer.ui.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.kingbull.musicplayer.ui.main.MainActivity;

/**
 * @author Kailash Dabhi
 * @date 27 April, 2017 10:29 AM
 */
public final class SplashActivity extends AppCompatActivity {
  private static final int REQUEST_CODE_PERMISSION = 97;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    askPermission();
  }

  private void askPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[] {
          Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.READ_PHONE_STATE
      }, REQUEST_CODE_PERMISSION);
    } else {
      gotoMainActivity();
    }
  }

  private void gotoMainActivity() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }

  @Override public void onRequestPermissionsResult(int requestCode, String permissions[],
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_CODE_PERMISSION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length == 3
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED
            && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
          gotoMainActivity();
        } else {
          finish();
        }
        return;
      }
    }
  }
}
