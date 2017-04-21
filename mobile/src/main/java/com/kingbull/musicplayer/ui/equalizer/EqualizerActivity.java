package com.kingbull.musicplayer.ui.equalizer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;

public final class EqualizerActivity extends BaseActivity<Equalizer.Presenter> {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(android.R.id.content, EqualizerFragment.instance(),
              EqualizerFragment.class.getSimpleName())
          .commit();
    }
  }

  @NonNull @Override protected PresenterFactory<Equalizer.Presenter> presenterFactory() {
    return new PresenterFactory.Equalizer();
  }

  @Override protected void onPresenterPrepared(Equalizer.Presenter presenter) {
  }
}
