package com.kingbull.musicplayer.ui.songlist;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Presenter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class SongListPresenter extends Presenter<SongList.View>
    implements SongList.Presenter {

  private HashMap<Music, List<Music>> songListHashMap;
  private Music[] albums;
  private CompositeSubscription compositeSubscription;

  @Override public void takeView(@NonNull SongList.View view) {
    super.takeView(view);
    compositeSubscription = new CompositeSubscription();
  }

  @Override public void onSongCursorLoadFinished(Cursor cursor) {
    Subscription subscription =
        Observable.just(cursor)
            .flatMap(new Func1<Cursor, Observable<List<Music>>>() {
              @Override public Observable<List<Music>> call(Cursor cursor) {
                return new Songs(cursor).toObservable();
              }
            })
            .doOnNext(new Action1<List<Music>>() {
              @Override public void call(List<Music> songs) {
                //Log.d(TAG, "onLoadFinished: " + songs.size());
                Collections.sort(songs, new Comparator<Music>() {
                  @Override public int compare(Music left, Music right) {
                    return left.media().title().compareTo(right.media().title());
                  }
                });
              }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<Music>>() {
              @Override public void onStart() {
                //mView.showProgress();
              }

              @Override public void onCompleted() {
                //mView.hideProgress();
              }

              @Override public void onError(Throwable throwable) {
                //mView.hideProgress();
                //Log.e(TAG, "onError: ", throwable);
              }

              @Override public void onNext(List<Music> songs) {
                songListHashMap = new SongGroup(songs).ofAlbum();
                albums =
                    songListHashMap.keySet().toArray(new Music[songListHashMap.keySet().size()]);
                view().setAlbumPager(albums);
                view().showSongs(songListHashMap.get(albums[0]));
              }
            });
    compositeSubscription.add(subscription);
  }

  @Override public void onAlbumSelected(int position) {
    view().showSongs(songListHashMap.get(albums[position]));
  }
}
