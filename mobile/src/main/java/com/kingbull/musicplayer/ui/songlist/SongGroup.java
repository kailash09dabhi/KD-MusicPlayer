package com.kingbull.musicplayer.ui.songlist;

import com.kingbull.musicplayer.domain.Song;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kailash Dabhi
 * @date 11/14/2016.
 */

public final class SongGroup {
  private final List<Song> songs;

  SongGroup(List<Song> songs) {
    this.songs = songs;
  }

  public HashMap<Song, List<Song>> ofAlbum() {
    HashMap<Song, List<Song>> songHashMap = new HashMap<>();
    HashMap<String, List<Song>> hashMap = new HashMap<>();
    for (Song song : songs) {
      String albumName = song.getAlbum();
      if (hashMap.containsKey(albumName)) {
        hashMap.get(albumName).add(song);
      } else {
        List<Song> list = new ArrayList<>();
        list.add(song);
        hashMap.put(albumName, list);
      }
    }
    for (Map.Entry<String, List<Song>> stringListEntry : hashMap.entrySet()) {
      songHashMap.put(stringListEntry.getValue().get(0), stringListEntry.getValue());
    }
    return songHashMap;
  }
}
