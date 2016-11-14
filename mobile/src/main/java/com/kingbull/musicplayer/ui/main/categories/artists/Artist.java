package com.kingbull.musicplayer.ui.main.categories.artists;

import android.database.Cursor;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface Artist {
  interface View extends Mvp.View {
    void showAlbums(List<ArtistItem> songs);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<Artist.View> {
    void onArtistCursorLoadFinished(Cursor cursor);
  }
}
