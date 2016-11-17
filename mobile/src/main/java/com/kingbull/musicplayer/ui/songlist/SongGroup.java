package com.kingbull.musicplayer.ui.songlist;

import com.kingbull.musicplayer.domain.Music;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kailash Dabhi
 * @date 11/14/2016.
 */

public final class SongGroup {
  private final List<Music> songs;

  SongGroup(List<Music> songs) {
    this.songs = songs;
  }

  public HashMap<Music, List<Music>> ofAlbum() {
    HashMap<Music, List<Music>> songHashMap = new HashMap<>();
    HashMap<String, List<Music>> hashMap = new HashMap<>();
    for (Music song : songs) {
      String albumName = song.album();
      if (hashMap.containsKey(albumName)) {
        hashMap.get(albumName).add(song);
      } else {
        List<Music> list = new ArrayList<>();
        list.add(song);
        hashMap.put(albumName, list);
      }
    }
    for (Map.Entry<String, List<Music>> stringListEntry : hashMap.entrySet()) {
      songHashMap.put(stringListEntry.getValue().get(0), stringListEntry.getValue());
    }
    return songHashMap;
  }
}
