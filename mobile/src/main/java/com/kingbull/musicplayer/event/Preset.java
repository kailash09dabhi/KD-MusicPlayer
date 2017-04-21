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

  private Preset(String presetName) {
    this.event = Event.NEW;
    equalizerPreset = null;
    this.presetName = presetName;
    reverb = null;
  }

  private Preset(EqualizerPreset equalizerPreset) {
    this.event = Event.CLICK;
    this.equalizerPreset = equalizerPreset;
    presetName = null;
    reverb = null;
  }

  private Preset(Reverb reverb) {
    this.event = Event.REVERB;
    this.reverb = reverb;
    equalizerPreset = null;
    presetName = null;
  }

  public static Preset Click(EqualizerPreset equalizerPreset) {
    return new Preset(equalizerPreset);
  }

  public static Preset Reverb(Reverb reverb) {
    return new Preset(reverb);
  }

  public static Preset New(String presetName) {
    return new Preset(presetName);
  }

  public Reverb reverb() {
    return reverb;
  }

  public String presetName() {
    if (event() == Event.CLICK) {
      throw new RuntimeException("Preset.Event.CLICK is not allowed to " + "call this method");
    }
    return presetName;
  }

  public int event() {
    return event;
  }

  public EqualizerPreset equalizerPreset() {
    if (event() == Event.NEW) {
      throw new RuntimeException("Preset.Event.NEW is not allowed to " + "call this method");
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
