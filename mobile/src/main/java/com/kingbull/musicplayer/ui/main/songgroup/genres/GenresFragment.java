/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.songgroup.genres;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kingbull.musicplayer.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.ContentValues.TAG;

public final class GenresFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
  private RecyclerView recyclerView;

  private CompositeSubscription mSubscriptions;

  public GenresFragment() {
    mSubscriptions = new CompositeSubscription();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_genres, null);
    setupView(view);
    return view;
  }

  private void setupView(View v) {
    recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_grid);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    getLoaderManager().initLoader(0, null, this);
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new GenresCursorLoader(getContext());
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    Subscription subscription =
        rx.Observable.just(cursor)
            .flatMap(new Func1<Cursor, rx.Observable<List<Genre>>>() {
              @Override public rx.Observable<List<Genre>> call(Cursor cursor) {
                List<Genre> genres = new ArrayList<>();
                if (cursor != null && cursor.getCount() > 0) {
                  cursor.moveToFirst();
                  do {
                    Genre genre = cursorToMusic(cursor);
                    genres.add(genre);
                  } while (cursor.moveToNext());
                }
                return rx.Observable.just(genres);
              }
            })
            .doOnNext(new Action1<List<Genre>>() {
              @Override public void call(List<Genre> songs) {
                Log.d(TAG, "onLoadFinished: " + songs.size());
                Collections.sort(songs, new Comparator<Genre>() {
                  @Override public int compare(Genre left, Genre right) {
                    return left.name().compareTo(right.name());
                  }
                });
              }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<Genre>>() {
              @Override public void onStart() {
                //mView.showProgress();
              }

              @Override public void onCompleted() {
                //mView.hideProgress();
              }

              @Override public void onError(Throwable throwable) {
                //mView.hideProgress();
                Log.e(TAG, "onError: ", throwable);
              }

              @Override public void onNext(List<Genre> songs) {
                //mView.onLocalMusicLoaded(genres);
                //mView.emptyView(genres.isEmpty());
                recyclerView.setAdapter(new GenresAdapter(songs));
              }
            });
    mSubscriptions.add(subscription);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
    // Empty
  }

  private Genre cursorToMusic(Cursor cursor) {
    Genre genre = new Genre();
    genre.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID)));
    genre.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME)));
    return genre;
  }
}
