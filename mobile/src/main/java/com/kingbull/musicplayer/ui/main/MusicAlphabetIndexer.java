package com.kingbull.musicplayer.ui.main;

import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.AlphabetIndexer;

/**
 * Handles comparisons in a different way because the AlbumList, Genre and Artist
 * name are stripped of some prefixes such as "a", "an", "the" and some symbols.
 */
final class MusicAlphabetIndexer extends AlphabetIndexer {
  public MusicAlphabetIndexer(Cursor cursor, int sortedColumnIndex, CharSequence alphabet) {
    super(cursor, sortedColumnIndex, alphabet);
  }

  @Override protected int compare(String word, String letter) {
    String wordKey = MediaStore.Audio.keyFor(word);
    String letterKey = MediaStore.Audio.keyFor(letter);
    if (wordKey.startsWith(letter)) {
      return 0;
    } else {
      return wordKey.compareTo(letterKey);
    }
  }
}
