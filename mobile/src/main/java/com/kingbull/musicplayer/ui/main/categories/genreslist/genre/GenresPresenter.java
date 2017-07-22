package com.kingbull.musicplayer.ui.main.categories.genreslist.genre;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.crashlytics.android.Crashlytics;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.domain.AlbumMusicsMap;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.MusicGroupOrder;
import com.kingbull.musicplayer.domain.SortBy;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;
import com.kingbull.musicplayer.ui.base.musiclist.AndroidMediaStoreDatabase;
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
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */
public final class GenresPresenter extends Presenter<Genre.View> implements Genre.Presenter {
  private final AndroidMediaStoreDatabase androidMediaStoreDatabase =
      new AndroidMediaStoreDatabase();
  private final List<Music> songs = new ArrayList<>();
  @Inject Player musicPlayer;
  private AlbumMusicsMap albumMusicsMap;
  private List<Album> albums = new ArrayList<>();
  private CompositeDisposable compositeDisposable;
  private int albumPosition;

  @Override public void takeView(@NonNull Genre.View view) {
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
                new MusicGroupOrder(songs).by(SortBy.TITLE);
              }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new ResourceSubscriber<List<Music>>() {
              @Override public void onNext(List<Music> musicList) {
                songs.clear();
                songs.addAll(musicList);
                albums.clear();
                if (musicList.size() > 0) {
                  albumMusicsMap = new AlbumMusicsMap(musicList);
                  albums.addAll(albumMusicsMap.keySet());
                  Album allSongs = new Album.Smart("All Songs");
                  albums.add(0, allSongs);
                  albumMusicsMap.put(allSongs, musicList);
                  view().setAlbumPager(albums);
                  view().showSongs(albumMusicsMap.get(allSongs));
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
    if (position >= 0) {
      albumPosition = position;
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
    view().clearSelection();
  }

  @Override public void onDeleteSelectedMusicClick() {
    List<SqlMusic> musicList = view().selectedMusicList();
    for (Music music : musicList) {
      String path = music.media().path();
      new File(path).delete();
      androidMediaStoreDatabase.deleteAndBroadcastDeletion(path);
      view().removeFromList(music);
    }
    view().showMessage(String.format("%d songs deleted successfully!", musicList.size()));
    view().clearSelection();
    view().hideSelectionContextOptions();
  }

  @Override public void onClearSelection() {
    view().hideSelectionOptions();
  }

  @Override public void onSortMenuClick() {
    view().showSortMusicListDialog();
  }

  @Override public void onShuffleMenuClick() {
    if (!songs.isEmpty()) {
      List<Music> musics = new ArrayList<>(songs);
      Collections.shuffle(musics);
      musicPlayer.addToNowPlaylist(musics);
      musicPlayer.play();
      view().showMusicScreen();
    }
  }

  @Override public void onSortEvent(SortEvent sortEvent) {
    if (albums.size() > 0 && albumPosition >= 0 && albumPosition < albums.size()) {
      List<Music> songs = albumMusicsMap.get(albums.get(albumPosition));
      new MusicGroupOrder(songs).by(sortEvent.sortBy());
      if (sortEvent.isSortInDescending()) Collections.reverse(songs);
      view().showSongs(songs);
    } else {
      Crashlytics.logException(new IndexOutOfBoundsException(
          "album size is " + albums.size() + " & albumPosition is " + albumPosition));
    }
  }
}
