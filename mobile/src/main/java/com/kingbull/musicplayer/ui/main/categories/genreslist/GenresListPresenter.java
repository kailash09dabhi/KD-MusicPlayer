package com.kingbull.musicplayer.ui.main.categories.genreslist;

import android.database.Cursor;
import androidx.annotation.NonNull;
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
public final class GenresListPresenter extends Presenter<GenresList.View>
    implements GenresList.Presenter {
  @Override public void takeView(@NonNull GenresList.View view) {
    super.takeView(view);
  }

  @Override public void onGenresCursorLoadFinished(Cursor cursor) {
    compositeDisposable.add(
        Flowable.just(cursor)
            .flatMap((Function<Cursor, Flowable<List<Genre>>>) cursor1 -> {
              List<Genre> genres = new ArrayList<>();
              if (cursor1 != null && cursor1.getCount() > 0) {
                cursor1.moveToFirst();
                do {
                  Genre genre = new Genre(cursor1);
                  genres.add(genre);
                } while (cursor1.moveToNext());
              }
              return Flowable.just(genres);
            })
            .doOnNext(songs -> {
              Log.d(TAG, "onLoadFinished: " + songs.size());
              Collections.sort(songs, (left, right) -> left.name().compareTo(right.name()));
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new ResourceSubscriber<List<Genre>>() {
              @Override public void onNext(List<Genre> genres) {
                view().showGenres(genres);
              }

              @Override public void onError(Throwable throwable) {
                //mView.hideProgress();
                Log.e(TAG, "onError: ", throwable);
              }

              @Override public void onComplete() {
              }
            }));
  }

  @Override public void onGenreClick(Genre genre) {
    view().gotoGenreScreen(genre);
  }
}
