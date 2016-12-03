package com.kingbull.musicplayer.player;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Kailash Dabhi
 * @date 10/5/2016.
 */
@IntDef({
    MusicPlayerEvent.PLAY, MusicPlayerEvent.PAUSE, MusicPlayerEvent.PREVIOUS, MusicPlayerEvent.NEXT,
    MusicPlayerEvent.STOP
}) @Retention(RetentionPolicy.SOURCE) public @interface MusicPlayerEvent {
  int PLAY = 0;
  int PAUSE = 1;
  int NEXT = 2;
  int PREVIOUS = 3;
  int STOP = 4;
}

