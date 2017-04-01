/*
 * Copyright (c) 2016. Kailash Dabhi (Kingbull Technology)
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.kingbull.musicplayer.domain.storage.preferences;

import android.content.SharedPreferences;
import android.media.audiofx.PresetReverb;
import android.preference.PreferenceManager;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.player.MusicMode;
import com.kingbull.musicplayer.ui.equalizer.reverb.Reverb;

public final class SettingPreferences {

  private final SharedPreferences settingsPrefs;

  public SettingPreferences() {
    this.settingsPrefs = PreferenceManager.getDefaultSharedPreferences(MusicPlayerApp.instance());
  }

  public MusicMode musicMode() {
    String playModeName = settingsPrefs.getString(Key.MUSIC_MODE, MusicMode.getDefault().name());
    return MusicMode.valueOf(playModeName);
  }

  public void saveMusicMode(MusicMode musicMode) {
    settingsPrefs.edit().putString(Key.MUSIC_MODE, musicMode.name()).apply();
  }

  public boolean isFullScreen() {
    return settingsPrefs.getBoolean(Key.IS_FULL_SCREEN, false);
  }

  public void saveFlatTheme(boolean isFlatTheme) {
    settingsPrefs.edit().putBoolean(Key.IS_FLAT_THEME, isFlatTheme).apply();
  }

  public boolean isFlatTheme() {
    return settingsPrefs.getBoolean(Key.IS_FLAT_THEME, false);
  }

  public void saveFullScreen(boolean isFullScreen) {
    settingsPrefs.edit().putBoolean(Key.IS_FULL_SCREEN, isFullScreen).apply();
  }

  public int lastChosenPresetId() {
    return settingsPrefs.getInt(Key.LAST_CHOSEN_PRESET_ID, 0);
  }

  public void saveFilterDurationInSeconds(int filterDurationInSeconds) {
    settingsPrefs.edit().putInt(Key.FILTER_DURATION_IN_SECONDS, filterDurationInSeconds).apply();
  }

  public int filterDurationInSeconds() {
    return settingsPrefs.getInt(Key.FILTER_DURATION_IN_SECONDS, 0);
  }

  public void saveLastChosenPresetId(int equalizerPreset) {
    settingsPrefs.edit().putInt(Key.LAST_CHOSEN_PRESET_ID, equalizerPreset).apply();
  }

  public boolean lastChosenPresetIsOfSystem() {
    return settingsPrefs.getBoolean(Key.LAST_CHOSEN_PRESET_IS_OF_SYSTEM, true);
  }

  public void saveLastChosenPresetIsOfSytem(boolean isOfSystem) {
    settingsPrefs.edit().putBoolean(Key.LAST_CHOSEN_PRESET_IS_OF_SYSTEM, isOfSystem).apply();
  }

  public void saveReverb(Reverb reverb) {
    settingsPrefs.edit().putInt(Key.REVERB_ID, reverb.id()).apply();
  }

  public Reverb reverb() {
    int reverbId = settingsPrefs.getInt(Key.REVERB_ID, PresetReverb.PRESET_NONE);
    if (reverbId == Reverb.LARGE_HALL.id()) {
      return Reverb.LARGE_HALL;
    } else if (reverbId == Reverb.LARGE_ROOM.id()) {
      return Reverb.LARGE_ROOM;
    } else if (reverbId == Reverb.MEDIUM_HALL.id()) {
      return Reverb.MEDIUM_HALL;
    } else if (reverbId == Reverb.MEDIUM_ROOM.id()) {
      return Reverb.MEDIUM_ROOM;
    } else if (reverbId == Reverb.SMALL_ROOM.id()) {
      return Reverb.SMALL_ROOM;
    } else if (reverbId == Reverb.PLATE.id()) {
      return Reverb.PLATE;
    } else {
      return Reverb.NONE;
    }
  }

  public final static class Key {
    public final static String MUSIC_MODE = "music_mode";
    public final static String IS_FULL_SCREEN = "full_screen";
    public final static String IS_FLAT_THEME = "flat_theme";
    public final static String LAST_CHOSEN_PRESET_IS_OF_SYSTEM = "last_chosen_preset_is_of_system";
    public final static String LAST_CHOSEN_PRESET_ID = "last_chosen_preset_id";
    public final static String REVERB_ID = "reverb_id";
    public final static String FILTER_DURATION_IN_SECONDS = "filter_duration_in_seconds";
  }
}
