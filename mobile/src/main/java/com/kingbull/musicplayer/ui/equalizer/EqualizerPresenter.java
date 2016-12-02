package com.kingbull.musicplayer.ui.equalizer;

import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.domain.SettingPreferences;
import com.kingbull.musicplayer.ui.base.Presenter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class EqualizerPresenter extends Presenter<Equalizer.View>
    implements Equalizer.Presenter {
  Equalizer.Model model;
  List<EqualizerPreset> equalizerPresetList = new ArrayList<>();
  List<EqualizerPreset> sqlPresetList;
  List<EqualizerPreset> systemPresetList;
  SettingPreferences settingPreferences = new SettingPreferences();

  @Override public void onTakeAudioSessionId(int audioSessionId) {
    model = new EqualizerModel(audioSessionId);
    systemPresetList = model.systemPresetList();
    sqlPresetList = model.sqlPresetList();
    equalizerPresetList.addAll(sqlPresetList);
    equalizerPresetList.addAll(systemPresetList);
    view().setupPresetList(equalizerPresetList);
    view().takeSelectedPreset(equalizerPresetList.indexOf(lastChosenPreset()));
  }

  private EqualizerPreset lastChosenPreset() {
    EqualizerPreset equalizerPreset = null;
    int lastChosenPresetId = settingPreferences.lastChosenPresetId();
    if (settingPreferences.lastChosenPresetIsOfSystem()) {
      for (EqualizerPreset preset : systemPresetList) {
        if (preset.id() == lastChosenPresetId) {
          equalizerPreset = preset;
          break;
        }
      }
    } else {
      for (EqualizerPreset preset : sqlPresetList) {
        if (preset.id() == lastChosenPresetId) {
          equalizerPreset = preset;
          break;
        }
      }
    }
    return equalizerPreset;
  }

  @Override public void onPresetSelected(int position) {
    equalizerPresetList.get(position).applyTo(model.equalizer());
    view().updateEqualizerView(equalizerPresetList.
        get(position));
    settingPreferences.saveLastChosenPresetId(equalizerPresetList.get(position).id());
    settingPreferences.saveLastChosenPresetIsOfSytem(
        systemPresetList.contains(equalizerPresetList.get(position)) ? true : false);
  }

  @Override public void onBandValueChange(short bandNumber, int percentageValue) {
    model.updateBand(bandNumber, percentageValue);
  }

  @Override public void onNewPresetClick() {
    view().saveEqualizerPreset();
    equalizerPresetList.clear();
    sqlPresetList = model.sqlPresetList();
    equalizerPresetList.addAll(sqlPresetList);
    equalizerPresetList.addAll(systemPresetList);
    //we assuming that the sql preset list is sorted by its updated_time so the first item of the
    // list would be the last saved @EqualizerPresenter .
    view().takeSelectedPreset(0);
  }
}

