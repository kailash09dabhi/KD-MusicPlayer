package com.kingbull.musicplayer.ui.equalizer.preset;

import com.kingbull.musicplayer.ui.base.Mvp;

/**
 * @author Kailash Dabhi
 * @date 11/23/2016.
 */

public interface Preset {
  interface View extends Mvp.View {
    void showCreatePresetScreen();
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<Preset.View> {
    void onCreateNewClick();
  }
}
