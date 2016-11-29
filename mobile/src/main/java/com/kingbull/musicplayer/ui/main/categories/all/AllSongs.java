package com.kingbull.musicplayer.ui.main.categories.all;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface AllSongs {
  interface View extends Mvp.View {
    void showAllSongs(List<Music> songs);

    void showMusicScreen();

    void showSettingsScreen();

    void showAddToPlayListDialog();

    void showSortMusicScreen();
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<AllSongs.View> {
    void onAllSongsCursorLoadFinished(Cursor cursor);

    void onSearchTextChanged(String text);

    void onExitSearchClick();

    void onShuffleMenuClick();

    void onSettingsMenuClick();

    void onAddToPlayListMenuClick();

    void onSortMenuClick();


    void onSortEvent(SortEvent sortEvent);
  }
}
