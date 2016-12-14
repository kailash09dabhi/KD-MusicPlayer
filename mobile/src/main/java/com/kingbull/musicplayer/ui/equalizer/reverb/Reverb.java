package com.kingbull.musicplayer.ui.equalizer.reverb;

/**
 * @author Kailash Dabhi
 * @date 12/4/2016.
 */

public interface Reverb {
  Reverb LARGE_HALL = new Reverb() {
    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_LARGEHALL;
    }

    @Override public String name() {
      return "Large Hall";
    }
  };
  Reverb LARGE_ROOM = new Reverb() {
    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_LARGEROOM;
    }

    @Override public String name() {
      return "Large Room";
    }
  };
  Reverb MEDIUM_HALL = new Reverb() {
    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_MEDIUMHALL;
    }

    @Override public String name() {
      return "Medium Hall";
    }
  };
  Reverb MEDIUM_ROOM = new Reverb() {
    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_MEDIUMROOM;
    }

    @Override public String name() {
      return "Medium Room";
    }
  };
  Reverb SMALL_ROOM = new Reverb() {
    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_SMALLROOM;
    }

    @Override public String name() {
      return "Small Room";
    }
  };
  Reverb NONE = new Reverb() {
    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_NONE;
    }

    @Override public String name() {
      return "None";
    }
  };
  Reverb PLATE = new Reverb() {
    @Override public short id() {
      return android.media.audiofx.PresetReverb.PRESET_PLATE;
    }

    @Override public String name() {
      return "Plate";
    }
  };

  short id();

  String name();
}
