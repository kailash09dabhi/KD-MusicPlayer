package com.kingbull.musicplayer.ui.equalizer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.Presenter;
import com.kingbull.musicplayer.ui.base.PresenterFactory;

public final class EqualizerActivity extends BaseActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(android.R.id.content,
              EqualizerFragment.instance(),
              EqualizerFragment.class.getSimpleName())
          .commit();
    }
  }

  @Override protected void onPresenterPrepared(Presenter presenter) {
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.Equalizer();
  }
}
