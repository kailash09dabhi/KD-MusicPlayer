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

    void takeChosenPreset(EqualizerPreset equalizerPreset);

    void saveEqualizerPreset(String name);
  }

  interface Model extends Mvp.Model {

    List<EqualizerPreset> systemPresetList();

    List<EqualizerPreset> sqlPresetList();

    android.media.audiofx.Equalizer equalizer();

    void updateBand(short bandNumber, int percentageValue);

    EqualizerPreset lastChosenPreset();
  }

  interface Presenter extends Mvp.Presenter<Equalizer.View> {

    void onPresetSelected(EqualizerPreset equalizerPreset);

    void onBandValueChange(short bandNumber, int percentageValue);

    void onNewPresetEvent(String presetName);
  }
}
