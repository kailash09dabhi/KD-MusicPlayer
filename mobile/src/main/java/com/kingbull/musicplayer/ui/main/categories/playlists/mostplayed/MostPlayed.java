package com.kingbull.musicplayer.ui.main.categories.playlists.mostplayed;

import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface MostPlayed {
  interface View extends Mvp.View {
    void showMostPlayedMusic(List<Music> playLists);
  }

  interface Model extends Mvp.Model {
    List<Music> mostPlayedMusicList();
  }

  interface Presenter extends Mvp.Presenter<MostPlayed.View> {
  }
}
