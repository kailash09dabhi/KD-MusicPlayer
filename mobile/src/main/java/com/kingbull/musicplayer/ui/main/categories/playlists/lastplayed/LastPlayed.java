package com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed;

import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface LastPlayed {
  interface View extends Mvp.View {
    void showLastPlayedMusic(List<Music> playLists);
  }

  interface Model extends Mvp.Model {
    List<Music> lastPlayedMusicList();
  }

  interface Presenter extends Mvp.Presenter<LastPlayed.View> {
  }
}
