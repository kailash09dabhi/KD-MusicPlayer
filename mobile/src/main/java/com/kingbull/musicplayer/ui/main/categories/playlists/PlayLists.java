package com.kingbull.musicplayer.ui.main.categories.playlists;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.event.PlaylistRenameEvent;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */
public interface PlayLists {
  interface View extends Mvp.View {
    void showAllPlaylist(List<PlayList> playLists);

    void refreshListOfPlaylist();

    void showMessage(String message);
  }

  interface Model extends Mvp.Model {
    List<PlayList> listOfPlayList();
  }

  interface Presenter extends Mvp.Presenter<PlayLists.View> {
    void onAllSongsCursorLoadFinished(Cursor cursor);

    void onPlaylistCreated(PlayList playlist);

    void onPlaylistRename(PlaylistRenameEvent event);
  }
}
