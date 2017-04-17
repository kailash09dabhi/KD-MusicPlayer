package com.kingbull.musicplayer.ui.main.categories.genreslist.genre;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * Created by Kailash Dabhi on 09-07-2016.
 * You can contact us at kailash09dabhi@gmail.com OR on skype(kailash.09)
 * Copyright (c) 2016 Kingbull Technology. AllPlaylist rights reserved.
 */
public interface Genre {
  interface View extends Mvp.View {
    void showSongs(List<Music> songs);

    void setAlbumPager(List<Album> albums);

    void showEmptyView();

    void showEmptyDueToDurationFilterMessage();

    void showSelectionOptions();

    void clearSelection();

    void hideSelectionOptions();

    List<SqlMusic> selectedMusicList();

    void removeFromList(Music music);

    void showMessage(String format);

    void hideSelectionContextOptions();

    void showAddToPlayListDialog();

    void showSortMusicListDialog();

    void showMusicScreen();
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<Genre.View> {
    void onSongCursorLoadFinished(Cursor cursor);

    void onAlbumSelected(int position);

    void onClearSelectionClick();

    void onMultiSelection(int selectionCount);

    void onAddToPlayListMenuClick();

    void onDeleteSelectedMusicClick();

    void onClearSelection();

    void onSortMenuClick();

    void onShuffleMenuClick();

    void onSortEvent(SortEvent sortEvent);
  }
}
