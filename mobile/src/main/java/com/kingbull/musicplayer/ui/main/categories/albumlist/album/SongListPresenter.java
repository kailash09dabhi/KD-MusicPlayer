package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Presenter;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.reactivestreams.Publisher;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class SongListPresenter extends Presenter<Album.View> implements Album.Presenter {

  private CompositeDisposable compositeDisposable;

  @Override public void takeView(@NonNull Album.View view) {
    super.takeView(view);
    compositeDisposable = new CompositeDisposable();
  }

  @Override public void onSongCursorLoadFinished(Cursor cursor) {
    compositeDisposable.add(
        Flowable.just(cursor)
            .flatMap(new Function<Cursor, Flowable<List<Music>>>() {
              @Override public Flowable<List<Music>> apply(Cursor cursor) {
                return new Songs(cursor).toFlowable();
              }
            })
            .doOnNext(new Consumer<List<Music>>() {
              @Override public void accept(List<Music> songs) {
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
            .subscribeWith(new ResourceSubscriber<List<Music>>() {
              @Override public void onNext(List<Music> musicList) {
                view().showSongs(musicList);
                compositeDisposable.add(Flowable.fromIterable(musicList)
                    .flatMap(new Function<Music, Publisher<Long>>() {
                      @Override public Publisher<Long> apply(Music music) throws Exception {
                        return Flowable.just(music.media().duration());
                      }
                    })
                    .toList()
                    .map(new Function<List<Long>, Long>() {
                      @Override public Long apply(List<Long> longs) throws Exception {
                        long sum = 0L;
                        for (long lng : longs)
                          sum = sum + lng;
                        return sum;
                      }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new ResourceSingleObserver<Long>() {

                      @Override public void onSuccess(Long value) {
                        view().showTotalDuration(new Milliseconds(value).toTimeString());
                      }

                      @Override public void onError(Throwable e) {
                      }
                    }));
              }

              @Override public void onError(Throwable e) {
              }

              @Override public void onComplete() {
              }
            }));
  }
}
