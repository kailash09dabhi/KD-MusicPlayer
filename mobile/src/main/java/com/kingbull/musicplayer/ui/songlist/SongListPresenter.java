package com.kingbull.musicplayer.ui.songlist;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Presenter;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */

public final class SongListPresenter extends Presenter<SongList.View>
    implements SongList.Presenter {

  private HashMap<Music, List<Music>> songListHashMap;
  private Music[] albums;
  private CompositeDisposable compositeDisposable;

  @Override public void takeView(@NonNull SongList.View view) {
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
                songListHashMap = new SongGroup(musicList).ofAlbum();
                albums =
                    songListHashMap.keySet().toArray(new Music[songListHashMap.keySet().size()]);
                view().setAlbumPager(albums);
                view().showSongs(songListHashMap.get(albums[0]));
              }

              @Override public void onError(Throwable e) {
              }

              @Override public void onComplete() {
              }
            }));
  }

  @Override public void onAlbumSelected(int position) {
    view().showSongs(songListHashMap.get(albums[position]));
  }
}
