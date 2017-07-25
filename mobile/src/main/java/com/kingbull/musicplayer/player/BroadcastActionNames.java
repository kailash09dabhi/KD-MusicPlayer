package com.kingbull.musicplayer.player;

import com.kingbull.musicplayer.MusicPlayerApp;

/**
 * @author Kailash Dabhi
 * @date 7/25/2017 4:20 PM
 */

public final class BroadcastActionNames {
  private final String stopAction;
  private final String playToggleAction;
  private final String playLastAction;
  private final String playNextAction;

  public BroadcastActionNames() {
    String packageName = MusicPlayerApp.instance().getPackageName();
    stopAction = packageName + ".ACTION_STOP_SERVICE";
    playToggleAction = packageName + ".ACTION_PLAY_TOGGLE";
    playLastAction = packageName + ".ACTION_PLAY_LAST";
    playNextAction = packageName + ".ACTION_PLAY_NEXT";
  }

  public String ofStop() {
    return stopAction;
  }

  public String ofPlayToggle() {
    return playToggleAction;
  }

  public String ofPlayLast() {
    return playLastAction;
  }

  public String ofPlayNext() {
    return playNextAction;
  }
}
