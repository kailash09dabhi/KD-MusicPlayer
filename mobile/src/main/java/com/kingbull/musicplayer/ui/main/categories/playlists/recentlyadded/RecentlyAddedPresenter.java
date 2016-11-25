package com.kingbull.musicplayer.ui.main.categories.playlists.recentlyadded;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.MediaCursor;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class RecentlyAddedPresenter extends Presenter<RecentlyAdded.View>
    implements RecentlyAdded.Presenter {

  @Inject Player musicPlayer;
  List<Music> songs;

  @Override public void takeView(@NonNull RecentlyAdded.View view) {
    super.takeView(view);
  }

  @Override public void onRecentlyAddedCursorLoadFinished(Cursor cursor) {
    Subscription subscription =
        Observable.just(cursor)
            .flatMap(new Func1<Cursor, Observable<List<Music>>>() {
              @Override public Observable<List<Music>> call(Cursor cursor) {
                List<Music> songs = new ArrayList<>();
                if (cursor != null && cursor.getCount() > 0) {
                  cursor.moveToFirst();
                  do {
                    Music song = new SqlMusic(new MediaCursor(cursor));
                    songs.add(song);
                  } while (cursor.moveToNext());
                }
                return Observable.just(songs);
              }
            })
            .doOnNext(new Action1<List<Music>>() {
              @Override public void call(List<Music> songs) {
                Log.d(TAG, "onLoadFinished: " + songs.size());
                Collections.sort(songs, new RecentlyAddedComparator());
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
                Log.e(TAG, "onError: ", throwable);
              }

              @Override public void onNext(List<Music> songs) {
                //mView.onLocalMusicLoaded(genres);
                //mView.emptyView(genres.isEmpty());
                view().showRecentlyAddedSongs(songs);
                RecentlyAddedPresenter.this.songs = songs;
              }
            });
    compositeSubscription.add(subscription);
  }

  @Override public void onPlayAllClick() {
    musicPlayer.addToNowPlaylist(songs);
  }
}
