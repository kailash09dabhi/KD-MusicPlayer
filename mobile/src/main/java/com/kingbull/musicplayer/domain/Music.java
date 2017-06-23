package com.kingbull.musicplayer.domain;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */
public interface Music {
  Media media();

  MediaStat mediaStat();

  interface Group {
    void sort(@SortBy int sortBy);
  }
}
