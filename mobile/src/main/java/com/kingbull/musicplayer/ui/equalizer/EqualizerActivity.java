package com.kingbull.musicplayer.ui.equalizer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public final class EqualizerActivity extends AppCompatActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(android.R.id.content,
              EqualizerFragment.instance(getIntent().getIntExtra("audio_session_id", 0)),
              EqualizerFragment.class.getSimpleName())
          .commit();
    }
  }
}
