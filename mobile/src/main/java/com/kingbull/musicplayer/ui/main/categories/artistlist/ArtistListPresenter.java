package com.kingbull.musicplayer.ui.main.categories.artistlist;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import com.kingbull.musicplayer.domain.Artist;
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

public final class ArtistListPresenter extends Presenter<ArtistList.View>
    implements ArtistList.Presenter {

  @Override public void takeView(@NonNull ArtistList.View view) {
    super.takeView(view);
  }

  @Override public void onArtistCursorLoadFinished(Cursor cursor) {
    if (cursor != null && cursor.getCount() > 0) {
      compositeDisposable.add(
          Flowable.just(cursor)
              .flatMap(new Function<Cursor, Flowable<List<Artist>>>() {
                @Override public Flowable<List<Artist>> apply(Cursor cursor) {
                  List<Artist> artistItems = new ArrayList<>();
                  if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                      Artist artist = new Artist.Smart(cursor);
                      artistItems.add(artist);
                    } while (cursor.moveToNext());
                  }
                  return Flowable.just(artistItems);
                }
              })
              .doOnNext(new Consumer<List<Artist>>() {
                @Override public void accept(List<Artist> songs) {
                  Log.d(TAG, "onLoadFinished: " + songs.size());
                  Collections.sort(songs, new Comparator<Artist>() {
                    @Override public int compare(Artist left, Artist right) {
                      return left.name().compareTo(right.name());
                    }
                  });
                }
              })
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeWith(new ResourceSubscriber<List<Artist>>() {
                @Override public void onNext(List<Artist> songs) {
                  //mView.onLocalMusicLoaded(genres);
                  //mView.emptyView(genres.isEmpty());
                  view().showAlbums(songs);
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

  @Override public void onArtistClick(Artist artist) {
    view().gotoArtistScreen(artist);
  }
}
