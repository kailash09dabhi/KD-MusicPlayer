package com.kingbull.musicplayer.ui.equalizer;

import android.media.audiofx.Equalizer;
import com.kingbull.musicplayer.domain.EqualizerPreset;

/**
 * @author Kailash Dabhi
 * @date 12/2/2016.
 */

public final class AudioFxEqualizerPreset implements EqualizerPreset {
  private final android.media.audiofx.Equalizer equalizer;
  private final short preset;

  AudioFxEqualizerPreset(Equalizer equalizer, short preset) {
    this.equalizer = equalizer;
    this.preset = preset;
  }

  @Override public int y1Percentage() {
    return percentageHeight(0);
  }

  @Override public int y2Percentage() {
    return percentageHeight(1);
  }

  @Override public int y3Percentage() {
    return percentageHeight(2);
  }

  @Override public int y4Percentage() {
    return percentageHeight(3);
  }

  @Override public int y5Percentage() {
    return percentageHeight(4);
  }

  @Override public String name() {
    return equalizer.getPresetName(preset);
  }

  private int percentageHeight(int bandIndex) {
    final short lowerEqualizerBandLevel = equalizer.getBandLevelRange()[0];
    final short upperEqualizerBandLevel = equalizer.getBandLevelRange()[1];
    short equalizerBandIndex = (short) bandIndex;
    float maxValue = upperEqualizerBandLevel - lowerEqualizerBandLevel;
    int selectedValue = equalizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;
    return (int) (selectedValue / maxValue * 100);
  }
}
