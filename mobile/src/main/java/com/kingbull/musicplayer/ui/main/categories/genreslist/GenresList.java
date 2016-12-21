package com.kingbull.musicplayer.ui.main.categories.genreslist;

import android.database.Cursor;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public interface GenresList {
  interface View extends Mvp.View {
    void showGenres(List<GenreList> songs);
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<GenresList.View> {
    void onGenresCursorLoadFinished(Cursor cursor);
  }
}
