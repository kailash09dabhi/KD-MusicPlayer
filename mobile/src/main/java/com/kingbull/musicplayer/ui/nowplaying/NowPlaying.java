package com.kingbull.musicplayer.ui.nowplaying;

import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/23/2016.
 */

public interface NowPlaying {
  interface View extends Mvp.View {
    void showNowPlayingList(List<Music> musicList, int jumpTo);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<NowPlaying.View> {
    void onShuffleClick();
  }
}
