package com.kingbull.musicplayer.ui.equalizer;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */
public final class EqualizerModel implements Equalizer.Model {
  private final android.media.audiofx.Equalizer equalizer;

  public EqualizerModel(int audioSessionId) {
    this.equalizer = new android.media.audiofx.Equalizer(5, audioSessionId);
    this.equalizer.setEnabled(true);
  }

  @Override public List<String> presetList() {
    ArrayList<String> equalizerPresetNames = new ArrayList<>();
    for (short i = 0; i < equalizer.getNumberOfPresets(); i++) {
      equalizerPresetNames.add(equalizer.getPresetName(i));
    }
    return equalizerPresetNames;
  }

  @Override public void updateEqualizerWithPreset(int position) {
    equalizer.usePreset((short) position);
  }

  @Override public android.media.audiofx.Equalizer equalizer() {
    return equalizer;
  }

  @Override public void updateBand(short bandNumber, int percentageValue) {
    Log.e("onBandValueChange", "band " + bandNumber + " percentage " + percentageValue);
    final short lowerEqualizerBandLevel = (short) equalizer.getBandLevelRange()[0];
    final short upperEqualizerBandLevel = (short) equalizer.getBandLevelRange()[1];
    final short maxBandLevel = (short) (upperEqualizerBandLevel - lowerEqualizerBandLevel);
    equalizer.setBandLevel(bandNumber,
        (short) (maxBandLevel * percentageValue / 100.0 + lowerEqualizerBandLevel));
  }
}
