package com.kingbull.musicplayer.domain;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */

public interface Music {
  int id();

  String title();

  String artist();

  String album();

  String path();

  long duration();

  int size();

  long dateAdded();

  boolean isFavorite();

  long numberOfTimesPlayed();

  long lastTimePlayed();
}
