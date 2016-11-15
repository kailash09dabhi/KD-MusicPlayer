package com.kingbull.musicplayer.ui.main.categories.playlists.recentlyadded;

import com.kingbull.musicplayer.domain.Song;
import java.util.Comparator;

/**
 * @author Kailash Dabhi
 * @date 11/15/2016.
 */

public class RecentlyAddedComparator implements Comparator<Song> {

  @Override public int compare(Song song1, Song song2) {
    long dateAddedSong1 = song1.dateAdded();
    long dateAddedSong2 = song2.dateAdded();
    if (dateAddedSong1 < dateAddedSong2) {
      return 1;
    } else if (dateAddedSong1 > dateAddedSong2) {
      return -1;
    } else {
      return 0;
    }
  }
}
