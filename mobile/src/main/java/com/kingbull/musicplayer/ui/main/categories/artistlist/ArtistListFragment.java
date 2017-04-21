
package com.kingbull.musicplayer.ui.main.categories.artistlist;

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
import com.kingbull.musicplayer.domain.Artist;
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
public final class ArtistListFragment extends BaseFragment<ArtistList.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, ArtistList.View {
  @BindView(R.id.recyclerView) RecyclerView recyclerView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_genres, container, false);
    ButterKnife.bind(this, view);
    setupView();
    return view;
  }

  private void setupView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setBackgroundColor(smartColorTheme.screen().intValue());
    recyclerView.setHasFixedSize(true);
    getLoaderManager().initLoader(0, null, this);
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

  @Override protected PresenterFactory<ArtistList.Presenter> presenterFactory() {
    return new PresenterFactory.ArtistList();
  }

  @Override protected void onPresenterPrepared(ArtistList.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new ArtistListCursorLoader(getContext());
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    presenter.onArtistCursorLoadFinished(cursor);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override public void showAlbums(List<Artist> songs) {
    recyclerView.setAdapter(new ArtistListAdapter(songs));
  }
}
