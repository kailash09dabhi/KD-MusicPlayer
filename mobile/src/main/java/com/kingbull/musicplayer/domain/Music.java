package com.kingbull.musicplayer.domain;

import android.os.Parcelable;

/**
 * Represents Song.
 *
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */
public interface Music extends Parcelable {
  Media media();

  MediaStat mediaStat();
}
