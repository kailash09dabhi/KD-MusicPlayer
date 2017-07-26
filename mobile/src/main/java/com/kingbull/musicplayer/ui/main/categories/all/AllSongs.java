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

    void showAddToPlayListDialog();

    void showSortMusicScreen();

    List<Music> selectedMusicList();

    void notifyItemRemoved(int position);

    void clearSelection();

    void hideSelectionContextOptions();

    void showMessage(String format);

    void refreshSongCount(int size);

    void showProgressLayout();

    void showAllSongsLayout();

    void percentage(int percentage);

    void deletedOutOfText(String s);

    void exitSearch();

    void enterSearch();
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<AllSongs.View> {
    void onAllSongsCursorLoadFinished(Cursor cursor);

    void onSearchTextChanged(String text);

    void onExitSearchClick();

    void onShuffleMenuClick();

    void onAddToPlayListMenuClick();

    void onSortMenuClick();

    void onSortEvent(SortEvent sortEvent);

    void onDeleteSelectedMusic();

    void onSearchClick();
  }
}
