package com.kingbull.musicplayer.ui.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;

/**
 * @author Kailash Dabhi
 * @date 11/26/2016.
 */

public final class SettingsActivity extends BaseActivity<Settings.Presenter> {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(android.R.id.content, new SettingsFragment(), SettingsFragment.class.getSimpleName())
          .commit();
    }
  }

  @NonNull @Override protected PresenterFactory<Settings.Presenter> presenterFactory() {
    return new PresenterFactory.Settings();
  }

  @Override protected void onPresenterPrepared(Settings.Presenter presenter) {
  }
}
