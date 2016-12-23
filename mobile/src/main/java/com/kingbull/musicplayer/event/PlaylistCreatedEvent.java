package com.kingbull.musicplayer.event;

import com.kingbull.musicplayer.domain.PlayList;

/**
 * @author Kailash Dabhi
 * @date 12/23/2016.
 */

public final class PlaylistCreatedEvent {
  private final PlayList playList;

  public PlaylistCreatedEvent(PlayList playList) {
    this.playList = playList;
  }

  public PlayList playList() {
    return playList;
  }
}
