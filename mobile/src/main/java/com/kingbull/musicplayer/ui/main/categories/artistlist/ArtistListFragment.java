package com.kingbull.musicplayer.ui.main.categories.artistlist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Artist;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.event.TransparencyChangedEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.ads.AdmobBannerLoaded;
import com.kingbull.musicplayer.ui.base.ads.AdmobInterstitial;
import com.kingbull.musicplayer.ui.main.categories.artistlist.artist.ArtistActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;

/**
 * Represents Artist list screen.
 *
 * @author Kailash Dabhi
 * @date 8th Nov, 2016 9:09 PM
 */
public final class ArtistListFragment extends BaseFragment<ArtistList.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, ArtistList.View {
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  private AdmobInterstitial admobInterstitial;
  private Artist lastClickedArtist;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_genres, container, false);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    new AdmobBannerLoaded((ViewGroup) view);
    setupView();
    setupInterstitial();
    return view;
  }

  private void setupView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            launchArtistActivity(lastClickedArtist);
          }
        });
    admobInterstitial.load();
  }

  private void launchArtistActivity(Artist artist) {
    Intent intent = new Intent(getActivity(), ArtistActivity.class);
    intent.putExtra("artist", (Parcelable) artist);
    getActivity().startActivity(intent);
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (presenter != null && presenter.hasView()) {
              if (o instanceof PaletteEvent || o instanceof ThemeEvent
                  || o instanceof TransparencyChangedEvent) {
                recyclerView.setBackgroundColor(smartTheme.screen().intValue());
              }
            } else {
              Crashlytics.logException(
                  new NullPointerException(
                      String.format(
                          "class: %s presenter- %s hasView- %b",
                          ArtistListFragment.class.getSimpleName(),
                          presenter, presenter != null && presenter.hasView()
                      )
                  )
              );
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
    recyclerView.setAdapter(new ArtistListAdapter(songs, presenter));
  }

  @Override public void gotoArtistScreen(Artist artist) {
    lastClickedArtist = artist;
    if (admobInterstitial.isLoaded()) {
      admobInterstitial.show();
    } else {
      launchArtistActivity(artist);
    }
  }
}
