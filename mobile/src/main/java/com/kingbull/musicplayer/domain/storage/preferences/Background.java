package com.kingbull.musicplayer.domain.storage.preferences;

import android.content.SharedPreferences;
import com.kingbull.musicplayer.ui.main.Pictures;

/**
 * Represents Background.
 *
 * @author Kailash Dabhi
 * @date 7/28/2017 6:45 PM
 */
public interface Background {
  int resId();

  void take(int index);

  void enableRandomMode();

  class Smart implements Background {
    private final static String BACKGROUND = "background";
    Pictures pictures = new Pictures();
    SharedPreferences sharedPreferences;

    public Smart(SharedPreferences sharedPreferences) {
      this.sharedPreferences = sharedPreferences;
    }

    @Override public int resId() {
      int backgroundIndex = sharedPreferences.getInt(BACKGROUND, -1);
      if (backgroundIndex == -1) {
        return pictures.random();
      } else {
        return pictures.toDrawablesId()[backgroundIndex];
      }
    }

    @Override public void take(int index) {
      sharedPreferences.edit().putInt(BACKGROUND, index).apply();
    }

    @Override public void enableRandomMode() {
      sharedPreferences.edit().putInt(BACKGROUND, -1).apply();
    }

  }
}
