package com.kingbull.musicplayer.ui.equalizer;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.analytics.Analytics;
import javax.inject.Inject;

public final class EqualizerActivity extends BaseActivity<Equalizer.Presenter> {
  @Inject Analytics analytics;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MusicPlayerApp.instance().component().inject(this);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(android.R.id.content, EqualizerFragment.instance(),
              EqualizerFragment.class.getSimpleName())
          .commit();
    }
    analytics.logScreen(EqualizerActivity.class.getSimpleName());
  }

  @NonNull @Override protected PresenterFactory<Equalizer.Presenter> presenterFactory() {
    return new PresenterFactory.Equalizer();
  }

  @Override protected void onPresenterPrepared(Equalizer.Presenter presenter) {
  }
}
