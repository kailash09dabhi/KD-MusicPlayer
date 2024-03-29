package com.kingbull.musicplayer.ui.main.categories.artistlist;

import android.database.Cursor;
import com.kingbull.musicplayer.domain.Artist;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface ArtistList {
  interface View extends Mvp.View {
    void showAlbums(List<Artist> songs);

    void gotoArtistScreen(Artist artist);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<ArtistList.View> {
    void onArtistCursorLoadFinished(Cursor cursor);

    void onArtistClick(Artist artist);
  }
}
