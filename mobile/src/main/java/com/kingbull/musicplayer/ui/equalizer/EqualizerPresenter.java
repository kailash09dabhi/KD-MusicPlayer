package com.kingbull.musicplayer.ui.equalizer;

import com.kingbull.musicplayer.ui.base.Presenter;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class EqualizerPresenter extends Presenter<Equalizer.View>
    implements Equalizer.Presenter {

  @Override public void onPresetSelected(int position) {
    view().updateEqualizer(position);
  }

  @Override public void onBandValueChange(short bandNumber, int percentageValue) {
    view().updateBand(bandNumber,percentageValue);
  }
}
