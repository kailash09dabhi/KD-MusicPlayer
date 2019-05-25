package com.kingbull.musicplayer.domain.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.IntDef;
import androidx.palette.graphics.Palette;
import com.kingbull.musicplayer.MusicPlayerApp;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.graphics.Color.BLACK;

/**
 * @author Kailash Dabhi
 * @date 1 April, 2017 1:16 PM
 */
public final class PalettePreference {
  private final SharedPreferences prefs;

  public PalettePreference() {
    this.prefs = MusicPlayerApp.instance()
        .getSharedPreferences(PalettePreference.class.getSimpleName(), Context.MODE_PRIVATE);
  }

  public Palette.Swatch swatch(@Swatch int swatchValue) {
    Palette.Swatch swatch = null;
    switch (swatchValue) {
      case Swatch.DARK_MUTED:
        swatch = darkMutedSwatch();
        break;
      case Swatch.DARK_VIBRANT:
        swatch = darkVibrantSwatch();
        break;
      case Swatch.DOMINANT:
        swatch = dominantSwatch();
        break;
      case Swatch.LIGHT_MUTED:
        swatch = lightMutedSwatch();
        break;
      case Swatch.LIGHT_VIBRANT:
        swatch = lightVibrantSwatch();
        break;
      case Swatch.MUTED:
        swatch = mutedSwatch();
        break;
      case Swatch.VIBRANT:
        swatch = vibrantSwatch();
        break;
    }
    return swatch;
  }

  private Palette.Swatch darkMutedSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.DARK_MUTED, BLACK),
        prefs.getInt(Key.DARK_MUTED_POPULATION, 0));
  }

  private Palette.Swatch darkVibrantSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.DARK_VIBRANT, BLACK),
        prefs.getInt(Key.DARK_VIBRANT_POPULATION, 0));
  }

  private Palette.Swatch dominantSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.DOMINANT, BLACK),
        prefs.getInt(Key.DOMINANT_POPULATION, 0));
  }

  private Palette.Swatch lightMutedSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.LIGHT_MUTED, BLACK),
        prefs.getInt(Key.LIGHT_MUTED_POPULATION, 0));
  }

  private Palette.Swatch lightVibrantSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.LIGHT_VIBRANT, BLACK),
        prefs.getInt(Key.LIGHT_VIBRANT_POPULATION, 0));
  }

  private Palette.Swatch mutedSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.MUTED, BLACK),
        prefs.getInt(Key.MUTED_POPULATION, 0));
  }

  private Palette.Swatch vibrantSwatch() {
    return new Palette.Swatch(prefs.getInt(Key.VIBRANT, BLACK),
        prefs.getInt(Key.VIBRANT_POPULATION, 0));
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

  @IntDef({
      Swatch.DARK_MUTED, Swatch.DARK_VIBRANT, Swatch.LIGHT_MUTED, Swatch.LIGHT_VIBRANT,
      Swatch.MUTED, Swatch.DOMINANT, Swatch.VIBRANT
  }) @Retention(RetentionPolicy.SOURCE) public @interface Swatch {
    int DARK_MUTED = 0;
    int DARK_VIBRANT = 1;
    int LIGHT_MUTED = 2;
    int LIGHT_VIBRANT = 3;
    int MUTED = 4;
    int DOMINANT = 5;
    int VIBRANT = 6;
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
