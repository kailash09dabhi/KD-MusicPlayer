package com.kingbull.musicplayer.ui.addtoplaylist;

import com.kingbull.musicplayer.ui.base.Presenter;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */

public final class AddToPlayListPresenter extends Presenter<AddToPlayList.View>
    implements AddToPlayList.Presenter {

  @Override public void onCreateNewClick() {
    view().showCreatePlaylistScreen();

  }
}
