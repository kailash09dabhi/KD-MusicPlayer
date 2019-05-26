package com.kingbull.musicplayer.ui.main.categories.artistlist;

import android.database.Cursor;
import androidx.annotation.NonNull;
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
              .flatMap((Function<Cursor, Flowable<List<Artist>>>) cursor1 -> {
                List<Artist> artistItems = new ArrayList<>();
                if (cursor1 != null && cursor1.getCount() > 0) {
                  cursor1.moveToFirst();
                  do {
                    Artist artist = new Artist.Smart(cursor1);
                    artistItems.add(artist);
                  } while (cursor1.moveToNext());
                }
                return Flowable.just(artistItems);
              })
              .doOnNext(songs -> {
                Log.d(TAG, "onLoadFinished: " + songs.size());
                Collections.sort(songs, (left, right) -> left.name().compareTo(right.name()));
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
