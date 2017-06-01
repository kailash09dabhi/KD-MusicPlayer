package com.kingbull.musicplayer.domain;

import android.media.audiofx.Equalizer;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlTableRow;

/**
 * @author Kailash Dabhi
 * @date 12/2/2016.
 */
public interface EqualizerPreset extends SqlTableRow {
  int y1Percentage();

  int y2Percentage();

  int y3Percentage();

  int y4Percentage();

  int y5Percentage();

  String name();

  void applyTo(Equalizer equalizer);

  int id();
}
