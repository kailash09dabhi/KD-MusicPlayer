package com.kingbull.musicplayer.event;

import com.kingbull.musicplayer.domain.PlayList;

/**
 * @author Kailash Dabhi
 * @date 4/2/2017
 */
public final class PlaylistRenameEvent {
  private final PlayList.Smart playList;
  private final String name;

  public PlaylistRenameEvent(PlayList.Smart playList, String name) {
    this.playList = playList;
    this.name = name;
  }

  public String name() {
    return name;
  }

  public PlayList.Smart playList() {
    return playList;
  }
}
