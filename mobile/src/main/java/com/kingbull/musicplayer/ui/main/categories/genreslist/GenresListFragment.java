package com.kingbull.musicplayer.ui.main.categories.genreslist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.event.TransparencyChangedEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.ads.AdmobBannerLoaded;
import com.kingbull.musicplayer.ui.base.ads.AdmobInterstitial;
import com.kingbull.musicplayer.ui.main.categories.genreslist.genre.GenreActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;

/**
 * Represents Genres list screen.
 *
 * @author Kailash Dabhi
 * @date 8th Nov, 2016 9:09 PM
 */
public final class GenresListFragment extends BaseFragment<GenresList.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, GenresList.View {
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  private AdmobInterstitial admobInterstitial;
  private Genre lastClickedGenre;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_genres, container, false);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    new AdmobBannerLoaded((ViewGroup) view);
    setupInterstitial();
    return view;
  }

  private void setupInterstitial() {
    admobInterstitial = new AdmobInterstitial(getActivity(),
        getResources().getString(R.string.kd_music_player_settings_interstitial),
        new FullScreenContentCallback() {
          @Override public void onAdClicked() {
            super.onAdClicked();
          }
        });
    admobInterstitial.show();
  }

  private void launchGenreActivity(Genre genre) {
    Intent intent = new Intent(getActivity(), GenreActivity.class);
    intent.putExtra("genre_id", genre.id());
    intent.putExtra("title", genre.name());
    getActivity().startActivity(intent);
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
        .subscribe(o -> {
          if (presenter != null && presenter.hasView()) {
            if (o instanceof PaletteEvent || o instanceof ThemeEvent
                || o instanceof TransparencyChangedEvent) {
              recyclerView.setBackgroundColor(smartTheme.screen().intValue());
            }
          } else {
            FirebaseCrashlytics.getInstance().recordException(
                new NullPointerException(
                    String.format(
                        "class: %s presenter- %s hasView- %b",
                        GenresListFragment.class.getSimpleName(),
                        presenter, presenter != null && presenter.hasView()
                    )
                )
            );
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
    recyclerView.setBackgroundColor(smartTheme.screen().intValue());
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    getLoaderManager().initLoader(0, null, this);
  }

  @Override public void showGenres(List<Genre> songs) {
    recyclerView.setAdapter(new GenresListAdapter(songs, presenter));
  }

  @Override public void gotoGenreScreen(Genre genre) {
    lastClickedGenre = genre;
    launchGenreActivity(genre);
  }
}
