package com.kingbull.musicplayer.ui.main.categories.playlists;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.kingbull.musicplayer.ui.base.Presenter;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class PlayListsPresenter extends Presenter<PlayLists.View>
    implements PlayLists.Presenter {

  PlayLists.Model model = new PlayListsModel();

  @Override public void takeView(@NonNull PlayLists.View view) {
    super.takeView(view);
    view().showAllPlaylist(model.listOfPlayList());
  }

  @Override public void onAllSongsCursorLoadFinished(Cursor cursor) {
  }
}
