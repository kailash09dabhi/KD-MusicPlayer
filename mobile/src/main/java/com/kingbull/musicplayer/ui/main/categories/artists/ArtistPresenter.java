package com.kingbull.musicplayer.ui.main.categories.artists;

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

public final class ArtistPresenter extends Presenter<Artist.View> implements Artist.Presenter {

  @Override public void takeView(@NonNull Artist.View view) {
    super.takeView(view);
  }

  @Override public void onArtistCursorLoadFinished(Cursor cursor) {
    compositeDisposable.add(
        Flowable.just(cursor)
            .flatMap(new Function<Cursor, Flowable<List<ArtistItem>>>() {
              @Override public Flowable<List<ArtistItem>> apply(Cursor cursor) {
                List<ArtistItem> artistItems = new ArrayList<>();
                if (cursor != null && cursor.getCount() > 0) {
                  cursor.moveToFirst();
                  do {
                    ArtistItem artistItem = new ArtistItem(cursor);
                    artistItems.add(artistItem);
                  } while (cursor.moveToNext());
                }
                return Flowable.just(artistItems);
              }
            })
            .doOnNext(new Consumer<List<ArtistItem>>() {
              @Override public void accept(List<ArtistItem> songs) {
                Log.d(TAG, "onLoadFinished: " + songs.size());
                Collections.sort(songs, new Comparator<ArtistItem>() {
                  @Override public int compare(ArtistItem left, ArtistItem right) {
                    return left.name().compareTo(right.name());
                  }
                });
              }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new ResourceSubscriber<List<ArtistItem>>() {

              @Override public void onError(Throwable throwable) {
                //mView.hideProgress();
                Log.e(TAG, "onError: ", throwable);
              }

              @Override public void onComplete() {
              }

              @Override public void onNext(List<ArtistItem> songs) {
                //mView.onLocalMusicLoaded(genres);
                //mView.emptyView(genres.isEmpty());
                view().showAlbums(songs);
              }
            }));
  }
}
