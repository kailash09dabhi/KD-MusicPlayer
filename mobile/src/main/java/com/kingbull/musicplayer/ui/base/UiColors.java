package com.kingbull.musicplayer.ui.base;

import com.kingbull.musicplayer.domain.storage.preferences.PalettePreference;

/**
 * @author Kailash Dabhi
 * @date 3/31/2017
 */
public final class UiColors {
  private final PalettePreference prefs = new PalettePreference();

  public UiColors() {
  }

  public Color window() {
    return new Color(prefs.darkVibrantColor());
  }

  public Color statusBar() {
    return window().dark(0.25f);
  }

  public Color tab() {
    return window().transparent(0.9f);
  }

  public Color screen() {
    return tab().dark(0.7f).transparent(0.9f);
  }
}
