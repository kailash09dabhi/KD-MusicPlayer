package com.kingbull.musicplayer.ui.base;

import android.support.annotation.IntDef;
import android.support.v7.graphics.Palette;
import com.kingbull.musicplayer.domain.storage.preferences.PalettePreference;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Kailash Dabhi
 * @date 3/31/2017
 */
public final class UiColors {
  private final PalettePreference prefs = new PalettePreference();
  @Swatch int swatch;

  public UiColors() {
    this.swatch = Swatch.DARK_MUTED;
  }

  public UiColors(@Swatch int swatch) {
    this.swatch = swatch;
  }

  public Color statusBar() {
    return screen().dark(0.25f);
  }

  public Color screen() {
    if (new SettingPreferences().isFlatTheme()) return new Color(swatch().getRgb());
    return new Color(swatch().getRgb()).transparent(0.9f);
  }

  public Palette.Swatch swatch() {
    Palette.Swatch swatch = null;
    switch (this.swatch) {
      case Swatch.DARK_MUTED:
        swatch = prefs.darkMutedSwatch();
        break;
      case Swatch.DARK_VIBRANT:
        swatch = prefs.darkVibrantSwatch();
        break;
      case Swatch.DOMINANT:
        swatch = prefs.dominantSwatch();
        break;
      case Swatch.LIGHT_MUTED:
        swatch = prefs.lightMutedSwatch();
        break;
      case Swatch.LIGHT_VIBRANT:
        swatch = prefs.lightVibrantSwatch();
        break;
      case Swatch.MUTED:
        swatch = prefs.mutedSwatch();
        break;
      case Swatch.VIBRANT:
        swatch = prefs.vibrantSwatch();
        break;
    }
    return swatch;
  }

  public Color quickAction() {
    if (new SettingPreferences().isFlatTheme()) return header();
    return new Color(swatch().getRgb());
  }

  public Color header() {
    if (new SettingPreferences().isFlatTheme()) return screen().light(0.16f);
    return screen().dark(0.16f).transparent(0.81f);
  }

  public Color dialog() {
    if (new SettingPreferences().isFlatTheme()) return screen().light(0.16f);
    return new Color(swatch().getRgb()).transparent(0.09f);
  }

  public Color tab() {
    return header();
  }

  public Color bodyTextColor() {
    return new Color(swatch().getBodyTextColor());
  }

  public Color titleTextColor() {
    return new Color(android.graphics.Color.WHITE);
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
}
