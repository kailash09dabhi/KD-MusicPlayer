package com.kingbull.musicplayer.ui.base;

import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;

/**
 * @author Kailash Dabhi
 * @date 3/31/2017
 */
public final class UiColors {
  private final SettingPreferences preferences = new SettingPreferences();

  public UiColors() {
  }

  public Color window() {
    return new Color(preferences.windowColor());
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
