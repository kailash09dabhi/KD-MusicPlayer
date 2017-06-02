package com.kingbull.musicplayer.ui.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.main.MainActivity;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */
public final class MusicPlayerActivity extends BaseActivity<MusicPlayer.Presenter> {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MusicPlayerApp.instance().component().inject(this);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(android.R.id.content, MusicPlayerFragment.newInstance(),
              MusicPlayerFragment.class.getSimpleName())
          .commit();
    }
  }

  @NonNull @Override protected PresenterFactory<MusicPlayer.Presenter> presenterFactory() {
    return new PresenterFactory.MusicPlayer();
  }

  @Override protected void onPresenterPrepared(MusicPlayer.Presenter presenter) {
  }

  @Override public void onBackPressed() {
    startActivity(new Intent(this, MainActivity.class));
    super.onBackPressed();
  }
}
