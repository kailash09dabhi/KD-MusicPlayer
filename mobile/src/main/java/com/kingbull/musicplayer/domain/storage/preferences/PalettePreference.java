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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import com.kingbull.musicplayer.MusicPlayerApp;

public final class PalettePreference {
  private final SharedPreferences prefs;

  public PalettePreference() {
    this.prefs = MusicPlayerApp.instance()
        .getSharedPreferences(PalettePreference.class.getSimpleName(), Context.MODE_PRIVATE);
  }

  public int mutedColor() {
    return prefs.getInt(Key.MUTED, Color.BLACK);
  }

  public int vibrantColor() {
    return prefs.getInt(Key.VIBRANT, Color.BLACK);
  }

  public int dominantColor() {
    return prefs.getInt(Key.DOMINANT, Color.BLACK);
  }

  public int lightMutedColor() {
    return prefs.getInt(Key.LIGHT_MUTED, Color.BLACK);
  }

  public int lightVibrantColor() {
    return prefs.getInt(Key.LIGHT_VIBRANT, Color.BLACK);
  }

  public int darkMutedColor() {
    return prefs.getInt(Key.DARK_MUTED, Color.BLACK);
  }

  public int darkVibrantColor() {
    return prefs.getInt(Key.DARK_VIBRANT, Color.BLACK);
  }

  public void save(Palette palette) {
    SharedPreferences.Editor editor = prefs.edit();
    editor.putInt(Key.MUTED, palette.getMutedColor(Color.BLACK));
    editor.putInt(Key.VIBRANT, palette.getVibrantColor(Color.BLACK));
    editor.putInt(Key.DOMINANT, palette.getDominantColor(Color.BLACK));
    editor.putInt(Key.LIGHT_MUTED, palette.getLightMutedColor(Color.BLACK));
    editor.putInt(Key.LIGHT_VIBRANT, palette.getLightVibrantColor(Color.BLACK));
    editor.putInt(Key.DARK_MUTED, palette.getDarkMutedColor(Color.BLACK));
    editor.putInt(Key.DARK_VIBRANT, palette.getDarkVibrantColor(Color.BLACK));
    editor.apply();
  }

  public final static class Key {
    public final static String MUTED = "muted";
    public final static String DOMINANT = "dominant";
    public final static String VIBRANT = "vibrant";
    public final static String LIGHT_MUTED = "light_muted";
    public final static String LIGHT_VIBRANT = "light_vibrant";
    public final static String DARK_MUTED = "dark_muted";
    public final static String DARK_VIBRANT = "dark_vibrant";
  }
}
