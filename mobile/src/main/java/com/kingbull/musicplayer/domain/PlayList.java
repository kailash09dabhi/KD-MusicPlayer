package com.kingbull.musicplayer.domain;

import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */

public interface PlayList {
  String name();

  List<Music> musicList();
}
