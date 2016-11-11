package com.kingbull.musicplayer.ui.main.songgroup.album;

import android.database.Cursor;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface Album {
  interface View extends Mvp.View {
    void showAlbums(List<AlbumItem> songs);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<Album.View> {
    void onAlbumCursorLoadFinished(Cursor cursor);
  }
}
