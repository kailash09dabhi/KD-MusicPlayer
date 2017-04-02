package com.kingbull.musicplayer.event;

import android.support.v7.graphics.Palette;

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
