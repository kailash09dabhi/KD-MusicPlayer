package com.kingbull.musicplayer.ui.main.categories.playlists.members;

import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface Members {
  interface View extends Mvp.View {
    void showPlaylistMembers(List<Music> musicList);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<Members.View> {
  }
}
