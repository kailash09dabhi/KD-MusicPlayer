package com.kingbull.musicplayer.ui.main.categories.albumlist;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface AlbumList {
  interface View extends Mvp.View {
    void showAlbums(List<Album> songs);

    void gotoAlbumScreen(Album album);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<AlbumList.View> {
    void onAlbumCursorLoadFinished(Cursor cursor);

    void onAlbumClick(Album album);
  }
}
