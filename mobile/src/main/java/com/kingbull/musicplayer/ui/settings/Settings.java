package com.kingbull.musicplayer.ui.settings;

import com.kingbull.musicplayer.ui.base.Mvp;

/**
 * @author Kailash Dabhi
 * @date 11/23/2016.
 */

public interface Settings {
  interface View extends Mvp.View {
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<Settings.View> {
  }
}
