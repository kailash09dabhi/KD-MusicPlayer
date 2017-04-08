package com.kingbull.musicplayer.ui.base.theme;

import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.ui.base.Color;

/**
 * @author Kailash Dabhi
 * @date 4/8/2017
 */
public interface ColorTheme {
  Color statusBar();

  Color header();

  Color screen();

  Color dialog();

  Color tab();

  Color quickAction();

  Color bodyText();

  Color titleText();

  class Smart extends AbstractColorTheme {
    ColorTheme.Transparent transparentTheme = new ColorTheme.Transparent();
    ColorTheme.Flat flatTheme = new ColorTheme.Flat();
    SettingPreferences settingPreferences = new SettingPreferences();

    @Override public Color header() {
      return settingPreferences.isFlatTheme() ? flatTheme.header() : transparentTheme.header();
    }

    @Override public Color screen() {
      return settingPreferences.isFlatTheme() ? flatTheme.screen() : transparentTheme.screen();
    }

    @Override public Color dialog() {
      return settingPreferences.isFlatTheme() ? flatTheme.dialog() : transparentTheme.dialog();
    }

    @Override public Color tab() {
      return settingPreferences.isFlatTheme() ? flatTheme.tab() : transparentTheme.tab();
    }

    @Override public Color quickAction() {
      return settingPreferences.isFlatTheme() ? flatTheme.quickAction()
          : transparentTheme.quickAction();
    }
  }

  class Flat extends AbstractColorTheme {
    @Override public Color header() {
      return screen().light(0.16f);
    }

    @Override public Color screen() {
      return new Color(prefs.swatch(type).getRgb());
    }

    @Override public Color dialog() {
      return header();
    }

    @Override public Color tab() {
      return header();
    }

    @Override public Color quickAction() {
      return header();
    }
  }

  class Transparent extends AbstractColorTheme {
    @Override public Color header() {
      return screen().dark(0.16f).transparent(0.81f);
    }

    @Override public Color screen() {
      return new Color(prefs.swatch(type).getRgb()).transparent(0.9f);
    }

    @Override public Color dialog() {
      return new Color(prefs.swatch(type).getRgb()).transparent(0.09f);
    }

    @Override public Color tab() {
      return header();
    }

    @Override public Color quickAction() {
      return new Color(prefs.swatch(type).getRgb());
    }
  }
}
