package com.kingbull.musicplayer.ui.main.songgroup.genres;

import android.database.Cursor;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface Genres {
  interface View extends Mvp.View {
    void showGenres(List<Genre> songs);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<Genres.View> {
    void onGenresCursorLoadFinished(Cursor cursor);
  }
}
