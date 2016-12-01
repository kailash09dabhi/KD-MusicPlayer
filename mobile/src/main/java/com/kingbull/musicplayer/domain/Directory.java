package com.kingbull.musicplayer.domain;

import android.support.v4.util.LruCache;
import com.kingbull.musicplayer.ui.main.categories.folder.AudioFileFilter;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

public final class Directory {

  private static final AudioFileFilter audioFileFilter = new AudioFileFilter();
  private static final LruCache<File, List<File>> cache = new LruCache<File, List<File>>(25) {
    @Override protected List<File> create(File key) {
      return Arrays.asList(key.listFiles(audioFileFilter));
    }
  };

  public List<File> audioFiles(File directory) {
    return cache.get(directory);
  }
}
