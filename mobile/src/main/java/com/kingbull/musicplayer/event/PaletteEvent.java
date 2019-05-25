package com.kingbull.musicplayer.event;

import androidx.palette.graphics.Palette;

/**
 * @author Kailash Dabhi
 * @date 3/31/2017
 */
public final class PaletteEvent {
  private final Palette palette;

  public PaletteEvent(Palette palette) {
    this.palette = palette;
  }
}
