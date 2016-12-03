package com.kingbull.musicplayer.ui.equalizer;

import android.support.annotation.NonNull;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.domain.SettingPreferences;
import com.kingbull.musicplayer.ui.base.Presenter;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class EqualizerPresenter extends Presenter<Equalizer.View>
    implements Equalizer.Presenter {
  Equalizer.Model model = new EqualizerModel();
  SettingPreferences settingPreferences = new SettingPreferences();

  @Override public void takeView(@NonNull Equalizer.View view) {
    super.takeView(view);
    view().takeChosenPreset(model.lastChosenPreset());
  }

  @Override public void onPresetSelected(EqualizerPreset equalizerPreset) {
    equalizerPreset.applyTo(model.equalizer());
    view().takeChosenPreset(equalizerPreset);
  }

  @Override public void onBandValueChange(short bandNumber, int percentageValue) {
    model.updateBand(bandNumber, percentageValue);
  }

  @Override public void onNewPresetEvent(String presetName) {
    view().saveEqualizerPreset(presetName);
    //we assuming that the sql preset list is sorted by its updated_time so the first item of the
    // list would be the last saved @EqualizerPresenter .
    view().takeChosenPreset(model.lastChosenPreset());
  }
}

