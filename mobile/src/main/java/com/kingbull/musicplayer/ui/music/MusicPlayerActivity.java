package com.kingbull.musicplayer.ui.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.ui.base.BaseActivity;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */

public final class MusicPlayerActivity extends BaseActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(android.R.id.content,
              MusicPlayerFragment.instance((Song) getIntent().getExtras().get("song")),
              MusicPlayerFragment.class.getSimpleName())
          .commit();
    }
  }
}
