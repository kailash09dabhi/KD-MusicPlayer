package com.kingbull.musicplayer.event;

import android.support.annotation.IntDef;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.ui.equalizer.reverb.Reverb;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Kailash Dabhi
 * @date 12/3/2016.
 */

public final class Preset {

  private final @Event int event;
  private final EqualizerPreset equalizerPreset;
  private final Reverb reverb;
  private final String presetName;

  public Preset(@Event int event, String presetName) {
    this.event = event;
    equalizerPreset = null;
    this.presetName = presetName;
    reverb = null;
  }

  public Preset(@Event int event, EqualizerPreset equalizerPreset) {
    this.event = event;
    this.equalizerPreset = equalizerPreset;
    presetName = null;
    reverb = null;
  }

  public Preset(@Event int event, Reverb reverb) {
    this.event = event;
    this.reverb = reverb;
    equalizerPreset = null;
    presetName = null;
  }

  public int event() {
    return event;
  }

  public Reverb reverb() {
    return reverb;
  }

  public String presetName() {
    if (event() == Event.CLICK) {
      throw new RuntimeException(
          "PresetReverb.Event.CLICK is not allowed to " + "call this method");
    }
    return presetName;
  }

  public EqualizerPreset equalizerPreset() {
    if (event() == Event.NEW) {
      throw new RuntimeException("PresetReverb.Event.NEW is not allowed to " + "call this method");
    }
    return equalizerPreset;
  }

  @IntDef({
      Event.CLICK, Event.NEW, Event.REVERB
  }) @Retention(RetentionPolicy.SOURCE) public @interface Event {
    int CLICK = 0;
    int NEW = 1;
    int REVERB = 2;
  }
}
