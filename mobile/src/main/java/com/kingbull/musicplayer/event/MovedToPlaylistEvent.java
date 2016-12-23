package com.kingbull.musicplayer.event;

/**
 * @author Kailash Dabhi
 * @date 12/23/2016.
 */

public final class MovedToPlaylistEvent {
  private final String destinationPlaylistName;
  private final int sourcePosition;

  public MovedToPlaylistEvent(int sourcePosition, String destinationPlaylistName) {
    this.sourcePosition = sourcePosition;
    this.destinationPlaylistName = destinationPlaylistName;
  }

  public String destinationPlaylistName() {
    return destinationPlaylistName;
  }

  public int position() {
    return sourcePosition;
  }
}
