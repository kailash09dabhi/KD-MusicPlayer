package com.kingbull.musicplayer.ui.base.theme;

import com.kingbull.musicplayer.domain.storage.preferences.PalettePreference;
import com.kingbull.musicplayer.ui.base.Color;

/**
 * @author Kailash Dabhi
 * @date 4/8/2017
 */
abstract class AbstractColorTheme implements ColorTheme {
  protected final @PalettePreference.Swatch int type = PalettePreference.Swatch.DARK_MUTED;
  protected final PalettePreference prefs = new PalettePreference();

  @Override public Color statusBar() {
    return screen().dark(0.25f);
  }

  @Override public Color bodyText() {
    return new Color(prefs.swatch(type).getBodyTextColor());
  }

  @Override public Color titleText() {
    return new Color(android.graphics.Color.WHITE);
  }
}
