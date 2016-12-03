package com.kingbull.musicplayer.ui.equalizer.reverb;

/**
 * @author Kailash Dabhi
 * @date 12/4/2016.
 */

public interface Reverb {
  short id();

  String name();

  class LargeHall implements Reverb {

    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_LARGEHALL;
    }

    @Override public String name() {
      return "Large Hall";
    }
  }

  class LargeRoom implements Reverb {

    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_LARGEROOM;
    }

    @Override public String name() {
      return "Large Room";
    }
  }

  class MediumHall implements Reverb {

    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_MEDIUMHALL;
    }

    @Override public String name() {
      return "Medium Hall";
    }
  }

  class MediumRoom implements Reverb {

    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_MEDIUMROOM;
    }

    @Override public String name() {
      return "Medium Room";
    }
  }

  class SmallRoom implements Reverb {

    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_SMALLROOM;
    }

    @Override public String name() {
      return "Small Room";
    }
  }

  class None implements Reverb {

    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_NONE;
    }

    @Override public String name() {
      return "None";
    }
  }

  class Plate implements Reverb {

    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_PLATE;
    }

    @Override public String name() {
      return "Plate";
    }
  }
}
