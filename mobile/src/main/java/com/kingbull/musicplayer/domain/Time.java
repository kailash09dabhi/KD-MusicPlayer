package com.kingbull.musicplayer.domain;

/**
 * @author Kailash Dabhi
 * @date 1/27/2017.
 */

public interface Time {

  Milliseconds milliSeconds();

  long difference(Time time);

  class Default implements Time {
    private final Milliseconds milliSeconds;

    public Default(Milliseconds milliSeconds) {
      this.milliSeconds = milliSeconds;
    }

    @Override public Milliseconds milliSeconds() {
      return milliSeconds;
    }

    @Override public long difference(Time time) {
      return Math.abs(milliSeconds.asLong() - time.milliSeconds().asLong());
    }
  }

  class Now implements Time {
    private final Milliseconds milliSeconds;

    public Now() {
      this.milliSeconds = new Milliseconds(System.currentTimeMillis());
    }

    @Override public Milliseconds milliSeconds() {
      return milliSeconds;
    }

    @Override public long difference(Time time) {
      return Math.abs(milliSeconds.asLong() - time.milliSeconds().asLong());
    }
  }
}
