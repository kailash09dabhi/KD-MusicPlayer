package com.kingbull.musicplayer.ui.splash;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.kingbull.musicplayer.ui.base.RequiredPermissions;
import com.kingbull.musicplayer.ui.main.MainActivity;

/**
 * @author Kailash Dabhi
 * @date 27 April, 2017 10:29 AM
 */
public final class SplashActivity extends AppCompatActivity {
  private static final int REQUEST_CODE_PERMISSION = 97;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RequiredPermissions requiredPermissions = new RequiredPermissions(this);
    if (requiredPermissions.isGranted()) {
      gotoMainActivity();
    } else {
      requiredPermissions.acquire(REQUEST_CODE_PERMISSION);
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
