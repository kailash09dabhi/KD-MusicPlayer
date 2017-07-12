package com.kingbull.musicplayer.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import com.kingbull.musicplayer.di.AppModule;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kailash Dabhi
 * @date 12/22/2016.
 */
public abstract class BaseDialogFragment extends DialogFragment {
  @Inject protected SettingPreferences settingPreferences;
  @Inject @Named(AppModule.SMART_THEME) protected ColorTheme smartTheme;

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    if (smartTheme != null) {
      getDialog().getWindow().setBackgroundDrawable(smartTheme.dialog().asDrawable());
    }
  }
}
