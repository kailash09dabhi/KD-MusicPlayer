package com.kingbull.musicplayer.ui.music;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */

public final class MusicPlayerActivity extends BaseActivity<MusicPlayer.Presenter> {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(android.R.id.content, MusicPlayerFragment.instance(),
              MusicPlayerFragment.class.getSimpleName())
          .commit();
    }
  }

  @Override protected void onPresenterPrepared(MusicPlayer.Presenter presenter) {
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.MusicPlayer();
  }
}
