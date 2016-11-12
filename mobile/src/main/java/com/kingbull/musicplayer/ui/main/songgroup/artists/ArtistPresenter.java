package com.kingbull.musicplayer.ui.main.songgroup.artists;

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

public final class ArtistPresenter extends Presenter<Artist.View> implements Artist.Presenter {

  private CompositeSubscription compositeSubscription;

  @Override public void takeView(@NonNull Artist.View view) {
    super.takeView(view);
    compositeSubscription = new CompositeSubscription();
  }

  @Override public void onArtistCursorLoadFinished(Cursor cursor) {
    Subscription subscription =
        Observable.just(cursor)
            .flatMap(new Func1<Cursor, Observable<List<ArtistItem>>>() {
              @Override public Observable<List<ArtistItem>> call(Cursor cursor) {
                List<ArtistItem> artistItems = new ArrayList<>();
                if (cursor != null && cursor.getCount() > 0) {
                  cursor.moveToFirst();
                  do {
                    ArtistItem artistItem = new ArtistItem(cursor);
                    artistItems.add(artistItem);
                  } while (cursor.moveToNext());
                }
                return Observable.just(artistItems);
              }
            })
            .doOnNext(new Action1<List<ArtistItem>>() {
              @Override public void call(List<ArtistItem> songs) {
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
            .subscribe(new Subscriber<List<ArtistItem>>() {
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

              @Override public void onNext(List<ArtistItem> songs) {
                //mView.onLocalMusicLoaded(genres);
                //mView.emptyView(genres.isEmpty());
                view().showAlbums(songs);
              }
            });
    compositeSubscription.add(subscription);
  }
}
