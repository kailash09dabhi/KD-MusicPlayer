package com.kingbull.musicplayer.ui.nowplaying;

import android.support.annotation.NonNull;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;
import java.util.Collections;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */
public final class NowPlayingPresenter extends Presenter<NowPlaying.View>
    implements NowPlaying.Presenter {
  @Inject Player player;

  @Override public void takeView(@NonNull NowPlaying.View view) {
    super.takeView(view);
    if (!player.nowPlayingMusicList().isEmpty()) {
      view().showNowPlayingList(player.nowPlayingMusicList(),
          player.nowPlayingMusicList().indexOf(player.nowPlayingMusicList().currentMusic()));
    }
  }

  @Override public void onShuffleClick() {
    if (!player.nowPlayingMusicList().isEmpty()) {
      Collections.shuffle(player.nowPlayingMusicList());
      view().showNowPlayingList(player.nowPlayingMusicList(),
          player.nowPlayingMusicList().indexOf(player.nowPlayingMusicList().currentMusic()));
      player.play(player.nowPlayingMusicList().currentMusic());
    }
  }
}

