package com.kingbull.musicplayer.ui.addtoplaylist;

import com.kingbull.musicplayer.ui.base.Mvp;

/**
 * @author Kailash Dabhi
 * @date 11/23/2016.
 */

public interface AddToPlayList {
  interface View extends Mvp.View {
    void showCreatePlaylistScreen();
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<AddToPlayList.View> {
    void onCreateNewClick();
  }
}
