package com.kingbull.musicplayer.ui.main.categories.genreslist;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import com.kingbull.musicplayer.ui.base.Presenter;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class GenresListPresenter extends Presenter<GenresList.View> implements GenresList.Presenter {

  @Override public void takeView(@NonNull GenresList.View view) {
    super.takeView(view);
  }

  @Override public void onGenresCursorLoadFinished(Cursor cursor) {
    compositeDisposable.add(
        Flowable.just(cursor)
            .flatMap(new Function<Cursor, Flowable<List<GenreList>>>() {
              @Override public Flowable<List<GenreList>> apply(Cursor cursor) {
                List<GenreList> genreLists = new ArrayList<>();
                if (cursor != null && cursor.getCount() > 0) {
                  cursor.moveToFirst();
                  do {
                    GenreList genreList = new GenreList(cursor);
                    genreLists.add(genreList);
                  } while (cursor.moveToNext());
                }
                return Flowable.just(genreLists);
              }
            })
            .doOnNext(new Consumer<List<GenreList>>() {
              @Override public void accept(List<GenreList> songs) {
                Log.d(TAG, "onLoadFinished: " + songs.size());
                Collections.sort(songs, new Comparator<GenreList>() {
                  @Override public int compare(GenreList left, GenreList right) {
                    return left.name().compareTo(right.name());
                  }
                });
              }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new ResourceSubscriber<List<GenreList>>() {

              @Override public void onNext(List<GenreList> genreLists) {
                view().showGenres(genreLists);
              }

              @Override public void onError(Throwable throwable) {
                //mView.hideProgress();
                Log.e(TAG, "onError: ", throwable);
              }

              @Override public void onComplete() {
              }
            }));
  }
}
