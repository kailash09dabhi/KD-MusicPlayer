package com.kingbull.musicplayer.event;

import java.util.concurrent.TimeUnit;

/**
 * @author Kailash Dabhi
 * @date 12/15/2016.
 */

public final class DurationFilterEvent {
  private final long seconds;

  public DurationFilterEvent(long seconds) {
    this.seconds = seconds;
  }

  public long durationInMilliseconds() {
    return TimeUnit.SECONDS.toMillis(seconds);
  }

  public long durationInSeconds() {
    return seconds;
  }
}
