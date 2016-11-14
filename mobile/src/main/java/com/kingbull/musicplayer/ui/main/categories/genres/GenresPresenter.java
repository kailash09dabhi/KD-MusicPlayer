package com.kingbull.musicplayer.ui.main.categories.genres;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import com.kingbull.musicplayer.ui.base.Presenter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.ContentValues.TAG;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class GenresPresenter extends Presenter<Genres.View> implements Genres.Presenter {

  private CompositeSubscription compositeSubscription;

  @Override public void takeView(@NonNull Genres.View view) {
    super.takeView(view);
    compositeSubscription = new CompositeSubscription();
  }

  @Override public void onGenresCursorLoadFinished(Cursor cursor) {
    Subscription subscription =
        Observable.just(cursor)
            .flatMap(new Func1<Cursor, Observable<List<Genre>>>() {
              @Override public Observable<List<Genre>> call(Cursor cursor) {
                List<Genre> genres = new ArrayList<>();
                if (cursor != null && cursor.getCount() > 0) {
                  cursor.moveToFirst();
                  do {
                    Genre genre = new Genre(cursor);
                    genres.add(genre);
                  } while (cursor.moveToNext());
                }
                return Observable.just(genres);
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
                view().showGenres(songs);
              }
            });
    compositeSubscription.add(subscription);
  }
}
