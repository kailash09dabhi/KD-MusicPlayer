package com.kingbull.musicplayer.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 23 June, 2017 12:14 AM
 */
public final class MusicGroup implements Music.Group {
  private final List<Music> songs;
  private Comparator<Music> byTitle = new Comparator<Music>() {
    @Override public int compare(Music song1, Music song2) {
      return song1.media().title().compareTo(song2.media().title());
    }
  };
  private Comparator<Music> byAlbum = new Comparator<Music>() {
    @Override public int compare(Music song1, Music song2) {
      return song1.media().album().compareTo(song2.media().album());
    }
  };
  private Comparator<Music> byDuration = new Comparator<Music>() {
    @Override public int compare(Music song1, Music song2) {
      long durationSong1 = song1.media().duration();
      long durationSong2 = song2.media().duration();
      if (durationSong1 < durationSong2) {
        return 1;
      } else if (durationSong1 > durationSong2) {
        return -1;
      } else {
        return 0;
      }
    }
  };
  private Comparator<Music> byArtist = new Comparator<Music>() {
    @Override public int compare(Music song1, Music song2) {
      return song1.media().artist().compareTo(song2.media().artist());
    }
  };
  private Comparator<Music> byDateAdded = new Comparator<Music>() {
    @Override public int compare(Music song1, Music song2) {
      long dateAddedSong1 = song1.media().dateAdded();
      long dateAddedSong2 = song2.media().dateAdded();
      if (dateAddedSong1 < dateAddedSong2) {
        return 1;
      } else if (dateAddedSong1 > dateAddedSong2) {
        return -1;
      } else {
        return 0;
      }
    }
  };
  private Comparator<Music> byYear = new Comparator<Music>() {
    @Override public int compare(Music song1, Music song2) {
      long yearSong1 = song1.media().year();
      long yearSong2 = song2.media().year();
      if (yearSong1 < yearSong2) {
        return 1;
      } else if (yearSong1 > yearSong2) {
        return -1;
      } else {
        return 0;
      }
    }
  };

  public MusicGroup(List<Music> musicList) {
    this.songs = musicList;
  }

  @Override public void sort(@SortBy int sortBy) {
    if (songs != null) {
      switch (sortBy) {
        case SortBy.ALBUM:
          Collections.sort(songs, byAlbum);
          break;
        case SortBy.ARTIST:
          Collections.sort(songs, byArtist);
          break;
        case SortBy.TITLE:
          Collections.sort(songs, byTitle);
          break;
        case SortBy.YEAR:
          Collections.sort(songs, byYear);
          break;
        case SortBy.DURATION:
          Collections.sort(songs, byDuration);
          break;
        case SortBy.DATE_ADDED:
          Collections.sort(songs, byDateAdded);
          break;
      }
    }
  }
}
