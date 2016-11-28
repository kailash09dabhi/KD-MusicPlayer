package com.kingbull.musicplayer.ui.main.categories.playlists.musics;

import android.support.annotation.NonNull;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class MusicListOfPlaylistsPresenter extends Presenter<MusicListOfPlaylist.View>
    implements MusicListOfPlaylist.Presenter {

  @Inject Player musicPlayer;

  @Override public void takeView(@NonNull MusicListOfPlaylist.View view) {
    super.takeView(view);
  }
}
