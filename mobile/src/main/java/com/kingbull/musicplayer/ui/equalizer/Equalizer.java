package com.kingbull.musicplayer.ui.equalizer;

import com.kingbull.musicplayer.ui.base.Mvp;

/**
 * @author Kailash Dabhi
 * @date 11/23/2016.
 */

public interface Equalizer {
  interface View extends Mvp.View {
    void updateEqualizer(int position);

    void updateBand(short bandNumber, int percentageValue);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<Equalizer.View> {

    void onPresetSelected(int position);

    void onBandValueChange(short bandNumber, int percentageValue);
  }
}
