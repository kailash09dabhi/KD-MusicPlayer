package com.kingbull.musicplayer.event;

import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.player.MusicPlayerEvent;

public final class MusicEvent {
  private final Music song;
  private final @MusicPlayerEvent int playerEvent;

  public MusicEvent(Music song, @MusicPlayerEvent int playerEvent) {
    this.song = song;
    this.playerEvent = playerEvent;
  }

  public Music music() {
    return song;
  }

  public int musicPlayerEvent() {
    return playerEvent;
  }
}
