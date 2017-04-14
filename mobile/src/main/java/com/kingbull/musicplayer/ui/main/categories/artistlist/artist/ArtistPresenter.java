package com.kingbull.musicplayer.ui.main.categories.artistlist.artist;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.domain.AlbumMusicsMap;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.ui.base.Presenter;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */
public final class ArtistPresenter extends Presenter<Artist.View> implements Artist.Presenter {
  AlbumMusicsMap albumMusicsMap;
  private List<Album> albums;
  private List<Music> allMusics;
  private CompositeDisposable compositeDisposable;

  @Override public void takeView(@NonNull Artist.View view) {
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
                allMusics = musicList;
                if (musicList.size() > 0) {
                  albumMusicsMap = new AlbumMusicsMap(musicList);
                  albums = new ArrayList<>(albumMusicsMap.keySet());
                  Album allSongs = new Album.Smart("All Songs");
                  albums.add(0, allSongs);
                  view().setAlbumPager(albums);
                  view().showSongs(musicList);
                } else {
                  view().showEmptyDueToDurationFilterMessage();
                }
              }

              @Override public void onError(Throwable e) {
                view().showEmptyView();
              }

              @Override public void onComplete() {
              }
            }));
  }

  @Override public void onAlbumSelected(int position) {
    if (position == 0) {
      view().showSongs(allMusics);
    } else {
      view().showSongs(albumMusicsMap.get(albums.get(position)));
    }
  }

  @Override public void onClearSelectionClick() {
    view().clearSelection();
    view().hideSelectionOptions();
  }

  @Override public void onMultiSelection(int selectionCount) {
    if (selectionCount == 1) {
      view().showSelectionOptions();
    }
  }

  @Override public void onAddToPlayListMenuClick() {
    view().showAddToPlayListDialog();
  }

  @Override public void onDeleteSelectedMusicClick() {
    List<SqlMusic> musicList = view().selectedMusicList();
    for (Music music : musicList) {
      new File(music.media().path()).delete();
      view().removeSongFromMediaStore(music);
      view().removeFromList(music);
    }
    view().showMessage(String.format("%d songs deleted successfully!", musicList.size()));
    view().clearSelection();
    view().hideSelectionContextOptions();
  }

  @Override public void onClearSelection() {
    view().hideSelectionOptions();
  }
}
