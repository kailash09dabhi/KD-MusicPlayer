package com.kingbull.musicplayer.ui.equalizer;

import android.util.Log;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.domain.storage.EqualizerPresetTable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */
public final class EqualizerModel implements Equalizer.Model {
  private final android.media.audiofx.Equalizer equalizer;
  @Inject EqualizerPresetTable equalizerPresetTable;

  public EqualizerModel(int audioSessionId) {
    this.equalizer = new android.media.audiofx.Equalizer(5, audioSessionId);
    this.equalizer.setEnabled(true);
    MusicPlayerApp.instance().component().inject(this);
  }


  @Override public List<EqualizerPreset> systemPresetList() {
    ArrayList<EqualizerPreset> equalizerPresets = new ArrayList<>();
    for (short i = 0; i < equalizer.getNumberOfPresets(); i++) {
      equalizerPresets.add(new AudioFxEqualizerPreset(equalizer, i));
    }
    return equalizerPresets;
  }

  @Override public List<EqualizerPreset> sqlPresetList() {
    return equalizerPresetTable.allPresets();
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
