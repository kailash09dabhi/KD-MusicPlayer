package com.kingbull.musicplayer.ui.base.theme;

import android.content.SharedPreferences;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.domain.storage.preferences.Transparency;
import com.kingbull.musicplayer.ui.base.Color;
import javax.inject.Inject;

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
    private final ColorTheme transparentTheme;
    private final ColorTheme flatTheme;
    SettingPreferences settingPreferences;

    @Inject public Smart(ColorTheme transparentTheme, ColorTheme flatTheme,
        SettingPreferences settingPreferences) {
      this.settingPreferences = settingPreferences;
      this.transparentTheme = transparentTheme;
      this.flatTheme = flatTheme;
    }

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
      return screen().light(0.25f);
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
    private final Transparency header;
    private final Transparency body;

    @Inject public Transparent(SharedPreferences sharedPreferences) {
      header = new Transparency.Header(sharedPreferences);
      body = new Transparency.Body(sharedPreferences);
    }

    @Override public Color header() {
      return new Color(prefs.swatch(type).getRgb()).dark(0.16f).transparent(header.value());
    }

    @Override public Color screen() {
      return new Color(android.graphics.Color.parseColor("#cc000000")).transparent(body.value());
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
