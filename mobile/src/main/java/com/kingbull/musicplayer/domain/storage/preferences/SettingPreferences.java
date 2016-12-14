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
import android.preference.PreferenceManager;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.player.MusicMode;

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

  public void saveFullScreen(boolean isFullScreen) {
    settingsPrefs.edit().putBoolean(Key.IS_FULL_SCREEN, isFullScreen).apply();
  }

  public int lastChosenPresetId() {
    return settingsPrefs.getInt(Key.LAST_CHOSEN_PRESET_ID, 0);
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

  public final static class Key {
    public final static String MUSIC_MODE = "music_mode";
    public final static String IS_FULL_SCREEN = "full_screen";
    public final static String LAST_CHOSEN_PRESET_IS_OF_SYSTEM = "last_chosen_preset_is_of_system";
    public final static String LAST_CHOSEN_PRESET_ID = "last_chosen_preset_id";
  }
}
