package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.database.Cursor;
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
public interface Album {
  interface View extends Mvp.View {
    void showSongs(List<Music> songs);

    void showTotalDuration(String value);

    void showPickOptions();

    void gotoGalleryScreen();

    void gotoInternetCoverArtsScreen();

    void showSortMusicListDialog();

    void showAddToPlayListDialog();

    void showMusicScreen();

    void clearSelection();

    List<SqlMusic> selectedMusicList();

    void removeSongFromMediaStore(Music music);

    void removeFromList(Music music);

    void showMessage(String format);

    void hideSelectionContextOptions();
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<Album.View> {
    void onSongCursorLoadFinished(Cursor cursor);

    void onCoverArtClick();

    void onPickFromInternetClick();

    void onPickFromGalleryClick();

    void onSortMenuClick();

    void onAddToPlayListMenuClick();

    void onShuffleMenuClick();

    void onSortEvent(SortEvent sortEvent);

    void onDeleteSelectedMusicClick();

    void onClearSelectionClick();
  }
}
