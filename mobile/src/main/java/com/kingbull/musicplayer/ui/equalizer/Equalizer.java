package com.kingbull.musicplayer.ui.equalizer;

import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/23/2016.
 */

public interface Equalizer {
  interface View extends Mvp.View {
    void updateEqualizerView(EqualizerPreset equalizerPreset);

    void setupPresetList(List<EqualizerPreset> presets);
  }

  interface Model extends Mvp.Model {
    List<EqualizerPreset> presetList();

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
