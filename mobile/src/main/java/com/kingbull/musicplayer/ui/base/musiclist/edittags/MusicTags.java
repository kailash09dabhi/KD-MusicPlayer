package com.kingbull.musicplayer.ui.base.musiclist.edittags;

import com.mpatric.mp3agic.NotSupportedException;
import java.io.IOException;

/**
 * @author Kailash Dabhi
 * @date 12/10/2016.
 */

public interface MusicTags {

  String artist();

  String genre();

  String year();

  String title();

  String album();

  Editor.Builder edit();

  interface Editor {
    void save() throws IOException, NotSupportedException;

    interface Builder {
      Builder artist(String artist);

      Builder genre(String genre);

      Builder year(String year);

      Builder title(String title);

      Builder album(String album);

      Editor build();
    }
  }
}
