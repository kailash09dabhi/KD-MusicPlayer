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
import android.support.v7.graphics.Palette;
import com.kingbull.musicplayer.MusicPlayerApp;

import static android.graphics.Color.BLACK;

public final class PalettePreference {
  private final SharedPreferences prefs;

  public PalettePreference() {
    this.prefs = MusicPlayerApp.instance()
        .getSharedPreferences(PalettePreference.class.getSimpleName(), Context.MODE_PRIVATE);
  }

  public Palette.Swatch mutedSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.MUTED, BLACK),
        prefs.getInt(Key.MUTED_POPULATION, 0));
  }

  public Palette.Swatch vibrantSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.VIBRANT, BLACK),
        prefs.getInt(Key.VIBRANT_POPULATION, 0));
  }

  public Palette.Swatch dominantSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.DOMINANT, BLACK),
        prefs.getInt(Key.DOMINANT_POPULATION, 0));
  }

  public Palette.Swatch lightMutedSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.LIGHT_MUTED, BLACK),
        prefs.getInt(Key.LIGHT_MUTED_POPULATION, 0));
  }

  public Palette.Swatch lightVibrantSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.LIGHT_VIBRANT, BLACK),
        prefs.getInt(Key.LIGHT_VIBRANT_POPULATION, 0));
  }

  public Palette.Swatch darkMutedSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.DARK_MUTED, BLACK),
        prefs.getInt(Key.DARK_MUTED_POPULATION, 0));
  }

  public Palette.Swatch darkVibrantSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.DARK_VIBRANT, BLACK),
        prefs.getInt(Key.DARK_VIBRANT_POPULATION, 0));
  }

  public void save(Palette palette) {
    SharedPreferences.Editor editor = prefs.edit();
    if (palette.getMutedSwatch() != null) {
      editor.putInt(Key.MUTED, palette.getMutedSwatch().getRgb());
      editor.putInt(Key.MUTED_POPULATION, palette.getMutedSwatch().getPopulation());
    }
    if (palette.getVibrantSwatch() != null) {
      editor.putInt(Key.VIBRANT, palette.getVibrantSwatch().getRgb());
      editor.putInt(Key.VIBRANT_POPULATION, palette.getVibrantSwatch().getPopulation());
    }
    if (palette.getDominantSwatch() != null) {
      editor.putInt(Key.DOMINANT, palette.getDominantSwatch().getRgb());
      editor.putInt(Key.DOMINANT_POPULATION, palette.getDominantSwatch().getPopulation());
    }
    if (palette.getLightMutedSwatch() != null) {
      editor.putInt(Key.LIGHT_MUTED, palette.getLightMutedSwatch().getRgb());
      editor.putInt(Key.LIGHT_MUTED_POPULATION, palette.getLightMutedSwatch().getPopulation());
    }
    if (palette.getLightVibrantSwatch() != null) {
      editor.putInt(Key.LIGHT_VIBRANT, palette.getLightVibrantSwatch().getRgb());
      editor.putInt(Key.LIGHT_VIBRANT_POPULATION, palette.getLightVibrantSwatch().getPopulation());
    }
    if (palette.getDarkMutedSwatch() != null) {
      editor.putInt(Key.DARK_MUTED, palette.getDarkMutedSwatch().getRgb());
      editor.putInt(Key.DARK_MUTED_POPULATION, palette.getDarkMutedSwatch().getPopulation());
    }
    if (palette.getDarkVibrantSwatch() != null) {
      editor.putInt(Key.DARK_VIBRANT, palette.getDarkVibrantSwatch().getRgb());
      editor.putInt(Key.DARK_VIBRANT_POPULATION, palette.getDarkVibrantSwatch().getPopulation());
    }
    editor.putInt(Key.MUTED, palette.getMutedColor(BLACK));
    editor.putInt(Key.VIBRANT, palette.getVibrantColor(BLACK));
    editor.putInt(Key.DOMINANT, palette.getDominantColor(BLACK));
    editor.putInt(Key.LIGHT_MUTED, palette.getLightMutedColor(BLACK));
    editor.putInt(Key.LIGHT_VIBRANT, palette.getLightVibrantColor(BLACK));
    editor.putInt(Key.DARK_MUTED, palette.getDarkMutedColor(BLACK));
    editor.putInt(Key.DARK_VIBRANT, palette.getDarkVibrantColor(BLACK));
    editor.apply();
  }

  public final static class Key {
    public final static String MUTED = "muted";
    public final static String MUTED_POPULATION = "muted_population";
    public final static String DOMINANT = "dominant";
    public final static String DOMINANT_POPULATION = "dominant_population";
    public final static String VIBRANT = "vibrant";
    public final static String VIBRANT_POPULATION = "vibrant_population";
    public final static String LIGHT_MUTED = "light_muted";
    public final static String LIGHT_MUTED_POPULATION = "light_muted_population";
    public final static String LIGHT_VIBRANT = "light_vibrant";
    public final static String LIGHT_VIBRANT_POPULATION = "light_vibrant_population";
    public final static String DARK_MUTED = "dark_muted";
    public final static String DARK_MUTED_POPULATION = "dark_muted_population";
    public final static String DARK_VIBRANT = "dark_vibrant";
    public final static String DARK_VIBRANT_POPULATION = "dark_vibrant_population";
  }
}
