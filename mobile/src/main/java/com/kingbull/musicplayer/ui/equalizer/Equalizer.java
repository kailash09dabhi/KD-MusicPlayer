package com.kingbull.musicplayer.ui.equalizer;

import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/23/2016.
 */

public interface Equalizer {
  interface View extends Mvp.View {
    void updateEqualizer(android.media.audiofx.Equalizer equalizer);

    void setupPresetList(List<String> presets);
  }

  interface Model extends Mvp.Model {
    List<String> presetList();

    void updateEqualizerWithPreset(int position);

    android.media.audiofx.Equalizer equalizer();

    void updateBand(short bandNumber, int percentageValue);
  }

  interface Presenter extends Mvp.Presenter<Equalizer.View> {
    void onTakeAudioSessionId(int audioSessionId);

    void onPresetSelected(int position);

    void onBandValueChange(short bandNumber, int percentageValue);
  }
}
