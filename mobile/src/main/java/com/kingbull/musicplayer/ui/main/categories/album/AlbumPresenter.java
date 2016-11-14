package com.kingbull.musicplayer.ui.main.categories.album;

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

public final class AlbumPresenter extends Presenter<Album.View> implements Album.Presenter {

  private CompositeSubscription compositeSubscription;

  @Override public void takeView(@NonNull Album.View view) {
    super.takeView(view);
    compositeSubscription = new CompositeSubscription();
  }

  @Override public void onAlbumCursorLoadFinished(Cursor cursor) {
    Subscription subscription =
        Observable.just(cursor)
            .flatMap(new Func1<Cursor, Observable<List<AlbumItem>>>() {
              @Override public Observable<List<AlbumItem>> call(Cursor cursor) {
                List<AlbumItem> albumItems = new ArrayList<>();
                if (cursor != null && cursor.getCount() > 0) {
                  cursor.moveToFirst();
                  do {
                    AlbumItem albumItem = new AlbumItem(cursor);
                    albumItems.add(albumItem);
                  } while (cursor.moveToNext());
                }
                return Observable.just(albumItems);
              }
            })
            .doOnNext(new Action1<List<AlbumItem>>() {
              @Override public void call(List<AlbumItem> songs) {
                Log.d(TAG, "onLoadFinished: " + songs.size());
                Collections.sort(songs, new Comparator<AlbumItem>() {
                  @Override public int compare(AlbumItem left, AlbumItem right) {
                    return left.name().compareTo(right.name());
                  }
                });
              }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<AlbumItem>>() {
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

              @Override public void onNext(List<AlbumItem> songs) {
                //mView.onLocalMusicLoaded(genres);
                //mView.emptyView(genres.isEmpty());
                view().showAlbums(songs);
              }
            });
    compositeSubscription.add(subscription);
  }
}
