package com.kingbull.musicplayer.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.kingbull.musicplayer.ui.main.MainActivity;

/**
 * @author Kailash Dabhi
 * @date 27 April, 2017 10:29 AM
 */
public final class SplashActivity extends AppCompatActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }
}
