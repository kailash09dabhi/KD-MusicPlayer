package com.kingbull.musicplayer.domain;

import android.util.Log;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaTable;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link FileMusicMap} is HashMap with File as Key and Music as Value.
 * But We should use music method which already contains the code which will take care of
 * creating music object if not exist. so needed no boiler plate code for this.
 *
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */
@Singleton public final class FileMusicMap extends HashMap<File, Music> {
  private static AtomicInteger counter = new AtomicInteger();
  @Inject MediaTable mediaTable;

  @Inject public FileMusicMap() {
    Log.e("instance counter", String.valueOf(counter.getAndIncrement()));
  }

  public Music music(File file) {
    Music song;
    if (containsKey(file)) {
      song = get(file);
    } else {
      song = new SqlMusic(mediaTable.mediaByFile(file));
      put(file, song);
    }
    return song;
  }
}
