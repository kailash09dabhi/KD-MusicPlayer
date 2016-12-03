package com.kingbull.musicplayer.ui.equalizer.reverb;

import com.kingbull.musicplayer.ui.base.Mvp;

/**
 * @author Kailash Dabhi
 * @date 11/23/2016.
 */

public interface PresetReverb {
  interface View extends Mvp.View {
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<PresetReverb.View> {
  }
}
