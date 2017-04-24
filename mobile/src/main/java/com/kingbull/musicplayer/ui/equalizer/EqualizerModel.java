package com.kingbull.musicplayer.ui.equalizer;

import android.util.Log;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.domain.storage.sqlite.table.EqualizerPresetTable;
import com.kingbull.musicplayer.player.Player;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */
public final class EqualizerModel implements Equalizer.Model {
  @Inject SettingPreferences settingPreferences;
  @Inject EqualizerPresetTable equalizerPresetTable;
  @Inject Player player;

  public EqualizerModel() {
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public List<EqualizerPreset> systemPresetList() {
    android.media.audiofx.Equalizer equalizer = player.equalizer();
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
    return player.equalizer();
  }

  @Override public void updateBand(short bandNumber, int percentageValue) {
    android.media.audiofx.Equalizer equalizer = player.equalizer();
    Log.e("onBandValueChange", "band " + bandNumber + " percentage " + percentageValue);
    final short lowerEqualizerBandLevel = equalizer.getBandLevelRange()[0];
    final short upperEqualizerBandLevel = equalizer.getBandLevelRange()[1];
    final short maxBandLevel = (short) (upperEqualizerBandLevel - lowerEqualizerBandLevel);
    equalizer.setBandLevel(bandNumber,
        (short) (maxBandLevel * percentageValue / 100.0 + lowerEqualizerBandLevel));
  }

  @Override public EqualizerPreset lastChosenPreset() {
    EqualizerPreset equalizerPreset = null;
    int lastChosenPresetId = settingPreferences.lastChosenPresetId();
    if (settingPreferences.lastChosenPresetIsOfSystem()) {
      List<EqualizerPreset> systemPresetList = systemPresetList();
      for (EqualizerPreset preset : systemPresetList) {
        if (preset.id() == lastChosenPresetId) {
          equalizerPreset = preset;
          break;
        }
      }
    } else {
      List<EqualizerPreset> sqlPresetList = sqlPresetList();
      for (EqualizerPreset preset : sqlPresetList) {
        if (preset.id() == lastChosenPresetId) {
          equalizerPreset = preset;
          break;
        }
      }
    }
    return equalizerPreset;
  }
}
