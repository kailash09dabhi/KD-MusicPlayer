package com.kingbull.musicplayer.ui.main.categories.playlists.recentlyadded;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface RecentlyAdded {
  interface View extends Mvp.View {
    void showRecentlyAddedSongs(List<Music> songs);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<RecentlyAdded.View> {
    void onRecentlyAddedCursorLoadFinished(Cursor cursor);

    void onPlayAllClick();
  }
}
