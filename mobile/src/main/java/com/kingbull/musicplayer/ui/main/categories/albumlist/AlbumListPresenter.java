package com.kingbull.musicplayer.ui.main.categories.albumlist;

import android.database.Cursor;
import androidx.annotation.NonNull;
import android.util.Log;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.ui.base.Presenter;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
public final class AlbumListPresenter extends Presenter<AlbumList.View>
    implements AlbumList.Presenter {
  private CompositeDisposable compositeDisposable;

  @Override public void takeView(@NonNull AlbumList.View view) {
    super.takeView(view);
    compositeDisposable = new CompositeDisposable();
  }

  @Override public void onAlbumCursorLoadFinished(Cursor cursor) {
    compositeDisposable.add(
        Flowable.just(cursor)
            .flatMap((Function<Cursor, Flowable<List<Album>>>) cursor1 -> {
              List<Album> albumItems = new ArrayList<>();
              if (cursor1 != null && cursor1.getCount() > 0) {
                cursor1.moveToFirst();
                do {
                  Album album = new Album.Smart(cursor1);
                  albumItems.add(album);
                } while (cursor1.moveToNext());
              }
              return Flowable.just(albumItems);
            })
            .doOnNext(songs -> {
              Log.d(TAG, "onLoadFinished: " + songs.size());
              Collections.sort(songs, (left, right) -> left.name().compareTo(right.name()));
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new ResourceSubscriber<List<Album>>() {
              @Override public void onNext(List<Album> songs) {
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

  @Override public void onAlbumClick(Album album) {
    view().gotoAlbumScreen(album);
  }
}
