package com.kingbull.musicplayer.domain.storage.preferences;

import android.content.SharedPreferences;
import android.media.audiofx.PresetReverb;
import com.kingbull.musicplayer.player.MusicMode;
import com.kingbull.musicplayer.ui.equalizer.reverb.Reverb;

/**
 * @author Kailash Dabhi
 * @date 27th Nov, 2016
 */
public final class SettingPreferences {
  private final SharedPreferences settingsPrefs;

  public SettingPreferences(SharedPreferences defaultSharedPrefs) {
    this.settingsPrefs = defaultSharedPrefs;
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

  public boolean pauseOnHeadsetUnplugged() {
    return settingsPrefs.getBoolean(Key.PAUSE_ON_HEADSET_UNPLUGGED, true);
  }

  public void pauseOnHeadsetUnplugged(boolean shouldPause) {
    settingsPrefs.edit().putBoolean(Key.PAUSE_ON_HEADSET_UNPLUGGED, shouldPause).apply();
  }

  public boolean resumeOnHeadsetPlugged() {
    return settingsPrefs.getBoolean(Key.RESUME_ON_HEADSET_PLUGGED, false);
  }

  public void resumeOnHeadsetPlugged(boolean shouldResume) {
    settingsPrefs.edit().putBoolean(Key.RESUME_ON_HEADSET_PLUGGED, shouldResume).apply();
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

  public void blurRadius(int blurRadius) {
    settingsPrefs.edit().putInt(Key.BLUR_RADIUS, blurRadius).apply();
  }

  public int blurRadius() {
    return settingsPrefs.getInt(Key.BLUR_RADIUS, 25);
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
    public final static String BLUR_RADIUS = "blur_radius";
    public final static String PAUSE_ON_HEADSET_UNPLUGGED = "pause_on_headset_unplugged";
    public final static String RESUME_ON_HEADSET_PLUGGED = "resume_on_headset_plugged";
  }
}
