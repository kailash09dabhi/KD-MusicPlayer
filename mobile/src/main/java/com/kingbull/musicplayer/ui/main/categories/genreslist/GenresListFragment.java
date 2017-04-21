package com.kingbull.musicplayer.ui.main.categories.genreslist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 8th Nov, 2016 9:09 PM
 */
public final class GenresListFragment extends BaseFragment<GenresList.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, GenresList.View {
  @BindView(R.id.recyclerView) RecyclerView recyclerView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_genres, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new GenresListCursorLoader(getContext());
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    presenter.onGenresCursorLoadFinished(cursor);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof PaletteEvent || o instanceof ThemeEvent) {
              recyclerView.setBackgroundColor(smartColorTheme.screen().intValue());
            }
          }
        });
  }

  @Override protected PresenterFactory<GenresList.Presenter> presenterFactory() {
    return new PresenterFactory.GenresList();
  }

  @Override protected void onPresenterPrepared(GenresList.Presenter presenter) {
    setupView();
    presenter.takeView(this);
  }

  private void setupView() {
    recyclerView.setBackgroundColor(smartColorTheme.screen().intValue());
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    getLoaderManager().initLoader(0, null, this);
  }

  @Override public void showGenres(List<GenreList> songs) {
    recyclerView.setAdapter(new GenresListAdapter(songs));
  }
}
