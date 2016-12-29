package com.kingbull.musicplayer.player;
/**
 * @author Kailash Dabhi
 * @date 12/25/2016.
 */
public enum MusicMode {
  REPEAT_NONE,
  REPEAT_SINGLE,
  REPEAT_ALL;

  public static MusicMode getDefault() {
    return REPEAT_NONE;
  }

  public static MusicMode switchNextMode(MusicMode current) {
    if (current == null) return getDefault();
    switch (current) {
      case REPEAT_SINGLE:
        return REPEAT_ALL;
      case REPEAT_ALL:
        return REPEAT_NONE;
      case REPEAT_NONE:
        return REPEAT_SINGLE;
    }
    return getDefault();
  }
}
