package com.kingbull.musicplayer.domain;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.MediaTable;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

public final class FileMusicMap extends HashMap<File, Music> {
  @Inject MediaTable mediaTable;

  public FileMusicMap() {
    MusicPlayerApp.instance().component().inject(this);
  }

  public Music music(File file) throws IOException {
    Music song;
    if (containsKey(file)) {
      song = get(file);
    } else {
      song = new SqlMusic(mediaTable.mediaByPath(file.getAbsolutePath()));
      put(file, song);
    }
    return song;
  }
}
