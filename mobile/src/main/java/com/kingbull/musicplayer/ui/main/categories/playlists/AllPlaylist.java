package com.kingbull.musicplayer.ui.main.categories.playlists;

import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.event.PlaylistRenameEvent;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */
public interface AllPlaylist {
  interface View extends Mvp.View {
    void showAllPlaylist(List<PlayList> playLists);

    void refreshListOfPlaylist();

    void showMessage(String message);

    void showPlaylist(PlayList playList);
  }

  interface Model extends Mvp.Model {
    List<PlayList> allPlaylist();
  }

  interface Presenter extends Mvp.Presenter<AllPlaylist.View> {
    void onPlaylistCreated(PlayList playlist);

    void onPlaylistRename(PlaylistRenameEvent event);

    void onPlaylistClick(PlayList playList);
  }
}
