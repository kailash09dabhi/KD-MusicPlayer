package com.kingbull.musicplayer.ui.main.categories.artistlist.artist;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * Created by Kailash Dabhi on 09-07-2016.
 * You can contact us at kailash09dabhi@gmail.com OR on skype(kailash.09)
 * Copyright (c) 2016 Kingbull Technology. PlayLists rights reserved.
 */
public interface Artist {
  interface View extends Mvp.View {
    void showSongs(List<Music> songs);

    void setAlbumPager(List<Album> albums);

    void showEmptyView();

    void showEmptyDueToDurationFilterMessage();
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<Artist.View> {
    void onSongCursorLoadFinished(Cursor cursor);

    void onAlbumSelected(int position);
  }
}
