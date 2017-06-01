package com.kingbull.musicplayer.ui.main.categories.albumlist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.ads.AdmobInterstitial;
import com.kingbull.musicplayer.ui.main.categories.albumlist.album.AlbumActivity;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 8th Nov, 2016
 */
public final class AlbumListFragment extends BaseFragment<AlbumList.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, AlbumList.View {
  @BindView(R.id.recyclerView) FastScrollRecyclerView recyclerView;
  private AdmobInterstitial admobInterstitial;
  private Album lastClickedAlbum;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_albumlist, container, false);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    setupView();
    setupInterstitial();
    return view;
  }

  private void setupView() {
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    recyclerView.setPopupBgColor(Color.WHITE);
    recyclerView.setThumbColor(Color.WHITE);
    int headerColor = smartTheme.header().intValue();
    recyclerView.setTrackColor(headerColor);
    recyclerView.setPopupTextColor(headerColor);
    recyclerView.setBackgroundColor(smartTheme.screen().intValue());
    recyclerView.setHasFixedSize(true);
    getLoaderManager().initLoader(0, null, this);
  }

  private void setupInterstitial() {
    admobInterstitial = new AdmobInterstitial(getActivity(),
        getResources().getString(R.string.kd_music_player_settings_interstitial),
        new AdmobInterstitial.AdListener() {
          @Override public void onAdClosed() {
            admobInterstitial.load();
            launchAlbumActivity(lastClickedAlbum);
          }
        });
    admobInterstitial.load();
  }

  private void launchAlbumActivity(Album album) {
    Intent intent = new Intent(getActivity(), AlbumActivity.class);
    intent.putExtra("album", (Parcelable) album);
    getActivity().startActivity(intent);
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof PaletteEvent || o instanceof ThemeEvent) {
              recyclerView.setBackgroundColor(smartTheme.screen().intValue());
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
    recyclerView.setAdapter(new AlbumListAdapter(songs, presenter));
  }

  @Override public void gotoAlbumScreen(Album album) {
    lastClickedAlbum = album;
    if (admobInterstitial.isLoaded()) {
      admobInterstitial.show();
    } else {
      launchAlbumActivity(album);
    }
  }
}
