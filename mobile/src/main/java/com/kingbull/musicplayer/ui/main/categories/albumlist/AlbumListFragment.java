/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.categories.albumlist;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;

public final class AlbumListFragment extends BaseFragment<AlbumList.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, AlbumList.View {
  @BindView(R.id.recyclerView) FastScrollRecyclerView recyclerView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_albumlist, null);
    ButterKnife.bind(this, view);
    setupView(view);
    return view;
  }

  private void setupView(View v) {
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    recyclerView.setPopupBgColor(Color.WHITE);
    recyclerView.setThumbColor(Color.WHITE);
    int headerColor = smartColorTheme.header().intValue();
    recyclerView.setTrackColor(headerColor);
    recyclerView.setPopupTextColor(headerColor);
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

  @Override protected PresenterFactory<AlbumList.Presenter> presenterFactory() {
    return new PresenterFactory.AlbumList();
  }

  @Override protected void onPresenterPrepared(AlbumList.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AlbumListCursorLoader(getContext());
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    presenter.onAlbumCursorLoadFinished(cursor);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
    // Empty
  }

  @Override public void showAlbums(List<Album> songs) {
    recyclerView.setAdapter(new AlbumListAdapter(songs));
  }
}
