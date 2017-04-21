package com.kingbull.musicplayer.domain;

import com.kingbull.musicplayer.domain.storage.sqlite.table.AlbumTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/14/2016.
 */

public final class AlbumMusicsMap extends HashMap<Album, List<Music>> {
  private AlbumTable albumTable = new AlbumTable();

  public AlbumMusicsMap(List<Music> songs) {
    for (Music music : songs) {
      Album album = albumTable.albumById(music.media().albumId());
      if (containsKey(album)) {
        get(album).add(music);
      } else {
        List<Music> list = new ArrayList<>();
        list.add(music);
        put(album, list);
      }
    }
  }
}
