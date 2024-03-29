package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.database.Cursor;
import androidx.annotation.NonNull;
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.MusicGroup;
import com.kingbull.musicplayer.domain.MusicGroupOrder;
import com.kingbull.musicplayer.domain.SortBy;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;
import com.kingbull.musicplayer.ui.base.musiclist.AndroidMediaStoreDatabase;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.reactivestreams.Publisher;

/**
 * Represents Album presenter.
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */
public final class AlbumPresenter extends Presenter<Album.View> implements Album.Presenter {
  private final AndroidMediaStoreDatabase androidMediaStoreDatabase =
      new AndroidMediaStoreDatabase();
  @Inject Player musicPlayer;
  private CompositeDisposable compositeDisposable;
  private List<Music> songs;

  @Override public void takeView(@NonNull Album.View view) {
    super.takeView(view);
    compositeDisposable = new CompositeDisposable();
  }

  @Override public void onSongCursorLoadFinished(Cursor cursor) {
    compositeDisposable.add(
        Flowable.just(cursor)
            .flatMap((Function<Cursor, Flowable<List<Music>>>) cursor1 -> Flowable.just(new MusicGroup.FromCursor(cursor1).asList()))
            .doOnNext(songs -> new MusicGroupOrder(songs).by(SortBy.TITLE))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new ResourceSubscriber<List<Music>>() {
              @Override public void onNext(List<Music> musicList) {
                AlbumPresenter.this.songs = musicList;
                view().showSongs(musicList);
                compositeDisposable.add(Flowable.fromIterable(musicList)
                    .flatMap((Function<Music, Publisher<Long>>) music -> Flowable.just(music.media().duration()))
                    .toList()
                    .map(longs -> {
                      long sum = 0L;
                      for (long lng : longs) {
                        sum = sum + lng;
                      }
                      return sum;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new ResourceSingleObserver<Long>() {
                      @Override public void onSuccess(Long value) {
                        view().showTotalDuration(new Milliseconds(value).toString());
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

  @Override public void onCoverArtClick() {
    view().showPickOptions();
  }

  @Override public void onPickFromInternetClick() {
    view().gotoInternetCoverArtsScreen();
  }

  @Override public void onPickFromGalleryClick() {
    view().gotoGalleryScreen();
  }

  @Override public void onSortMenuClick() {
    view().showSortMusicListDialog();
  }

  @Override public void onAddToPlayListMenuClick() {
    view().showAddToPlayListDialog();
  }

  @Override public void onShuffleMenuClick() {
    List<Music> musics = new ArrayList<>(songs);
    Collections.shuffle(musics);
    musicPlayer.addToNowPlaylist(musics);
    musicPlayer.play();
    view().showMusicScreen();
  }

  @Override public void onSortEvent(SortEvent sortEvent) {
    new MusicGroupOrder(songs).by(sortEvent.sortBy());
    if (sortEvent.isSortInDescending()) {
      Collections.reverse(songs);
    }
    view().showSongs(songs);
  }

  @Override public void onDeleteSelectedMusicClick() {
    List<Music> musicList = view().selectedMusicList();
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

  @Override public void onClearSelectionClick() {
    view().clearSelection();
  }
}
