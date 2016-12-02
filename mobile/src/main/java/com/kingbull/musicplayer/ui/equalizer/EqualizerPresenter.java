package com.kingbull.musicplayer.ui.equalizer;

import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.ui.base.Presenter;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class EqualizerPresenter extends Presenter<Equalizer.View>
    implements Equalizer.Presenter {
  Equalizer.Model model;
  List<EqualizerPreset> presetList;

  @Override public void onTakeAudioSessionId(int audioSessionId) {
    model = new EqualizerModel(audioSessionId);
    presetList = model.presetList();
    view().setupPresetList(presetList);
  }

  @Override public void onPresetSelected(int position) {
    model.updateEqualizerWithPreset(position);
    view().updateEqualizerView(presetList.get(position));
  }

  @Override public void onBandValueChange(short bandNumber, int percentageValue) {
    model.updateBand(bandNumber, percentageValue);
  }
}

