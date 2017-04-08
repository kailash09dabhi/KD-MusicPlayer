package com.kingbull.musicplayer.ui.main.categories.playlists;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.event.PlaylistRenameEvent;
import com.kingbull.musicplayer.ui.base.Presenter;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */
public final class AllPlayListPresenter extends Presenter<AllPlaylist.View>
    implements AllPlaylist.Presenter {
  AllPlaylist.Model model = new AllPlaylistModel();
  List<PlayList> listOfPlayList;

  @Override public void takeView(@NonNull AllPlaylist.View view) {
    super.takeView(view);
    listOfPlayList = model.allPlaylist();
    view().showAllPlaylist(listOfPlayList);
  }

  @Override public void onAllPlaylistCursorLoadFinished(Cursor cursor) {
  }

  @Override public void onPlaylistCreated(PlayList playlist) {
    listOfPlayList.add(playlist);
    view().showMessage(String.format("Playlist created with name \" %s \"", playlist.name()));
    view().refreshListOfPlaylist();
  }

  @Override public void onPlaylistRename(PlaylistRenameEvent event) {
    int index = listOfPlayList.indexOf(event.playList());
    listOfPlayList.remove(index);
    PlayList playList = event.playList().rename(event.name());
    listOfPlayList.add(index, playList);
    view().refreshListOfPlaylist();
  }
}
