package com.kingbull.musicplayer.event;

/**
 * @author Kailash Dabhi
 * @date 22 April, 2017 11:53 PM
 */
public final class BlurRadiusEvent {
  private final int radius;

  public BlurRadiusEvent(int radius) {
    this.radius = radius;
  }

  public int blurRadius() {
    return radius;
  }
}
