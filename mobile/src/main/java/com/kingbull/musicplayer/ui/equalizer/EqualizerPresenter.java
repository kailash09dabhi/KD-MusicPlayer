package com.kingbull.musicplayer.ui.equalizer;

import com.kingbull.musicplayer.ui.base.Presenter;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class EqualizerPresenter extends Presenter<Equalizer.View>
    implements Equalizer.Presenter {
  Equalizer.Model model;

  @Override public void onTakeAudioSessionId(int audioSessionId) {
    model = new EqualizerModel(audioSessionId);
    view().setupPresetList(model.presetList());
  }

  @Override public void onPresetSelected(int position) {
    model.updateEqualizerWithPreset(position);
    view().updateEqualizer(model.equalizer());
  }

  @Override public void onBandValueChange(short bandNumber, int percentageValue) {
    model.updateBand(bandNumber, percentageValue);
  }
}

