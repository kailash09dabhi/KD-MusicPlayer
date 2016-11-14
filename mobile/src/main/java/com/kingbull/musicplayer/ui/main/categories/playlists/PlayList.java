package com.kingbull.musicplayer.ui.main.categories.playlists;

import com.kingbull.musicplayer.domain.Song;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/14/2016.
 */

public final class PlayList {
  private String name;
  private List<Song> songs;
  PlayList(String name){
    this.name = name;
  }

  public String name() {
    return name;
  }

  public List<Song> songs() {
    return songs;
  }
}
