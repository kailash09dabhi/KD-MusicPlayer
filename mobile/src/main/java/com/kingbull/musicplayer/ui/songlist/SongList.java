package com.kingbull.musicplayer.ui.songlist;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * Created by Kailash Dabhi on 09-07-2016.
 * You can contact us at kailash09dabhi@gmail.com OR on skype(kailash.09)
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
 */
public interface SongList {
  interface View extends Mvp.View {
    void showSongs(List<Song> songs);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<SongList.View> {
    void onSongCursorLoadFinished(Cursor cursor);
  }
}
