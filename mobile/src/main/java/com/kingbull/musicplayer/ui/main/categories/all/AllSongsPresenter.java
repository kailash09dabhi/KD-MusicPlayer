package com.kingbull.musicplayer.ui.main.categories.all;

import android.database.Cursor;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.MusicGroup;
import com.kingbull.musicplayer.domain.MusicGroupOrder;
import com.kingbull.musicplayer.domain.SortBy;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;
import com.kingbull.musicplayer.ui.base.musiclist.AndroidMediaStoreDatabase;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */
public final class AllSongsPresenter extends Presenter<AllSongs.View>
    implements AllSongs.Presenter {
  @VisibleForTesting AndroidMediaStoreDatabase androidMediaStoreDatabase =
      new AndroidMediaStoreDatabase();
  @Inject Player musicPlayer;
  @VisibleForTesting List<Music> songs;

  @Override public void onAllSongsCursorLoadFinished(Cursor cursor) {
    if (cursor != null && cursor.getCount() > 0) {
      if (cursor.isClosed()) {
        if (isSongListAvailable()) {
          musicPlayer.addToNowPlaylist(songs);
          view().showAllSongs(songs);
        }
      } else {
        compositeDisposable.add(
            Flowable.just(cursor)
                .flatMap(new Function<Cursor, Flowable<List<Music>>>() {
                  @Override public Flowable<List<Music>> apply(Cursor cursor) {
                    return Flowable.just(new MusicGroup.FromCursor(cursor).asList());
                  }
                })
                .doOnNext(new Consumer<List<Music>>() {
                  @Override public void accept(List<Music> songs) {
                    new MusicGroupOrder(songs).by(SortBy.TITLE);
                  }
                })
                .filter(new Predicate<List<Music>>() {
                  @Override
                  public boolean test(@io.reactivex.annotations.NonNull List<Music> musics)
                      throws Exception {
                    return musics != null;
                  }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<List<Music>>() {
                  @Override public void onNext(List<Music> songs) {
                    AllSongsPresenter.this.songs = songs;
                    musicPlayer.addToNowPlaylist(songs);
                    view().showAllSongs(songs);
                  }

                  @Override public void onError(Throwable throwable) {
                    Log.e(AllSongsPresenter.class.getSimpleName(), "onError: ", throwable);
                  }

                  @Override public void onComplete() {
                  }
                }));
      }
    }
  }

   boolean isSongListAvailable() {
    return songs != null && !songs.isEmpty();
  }

  @Override public void onSearchTextChanged(final String text) {
    if (isSongListAvailable()) {
      compositeDisposable.add(Flowable.fromIterable(songs)
          .subscribeOn(Schedulers.computation())
          .filter(new Predicate<Music>() {
            @Override public boolean test(Music music) {
              return music.media().title().toLowerCase().contains(text.toLowerCase());
            }
          })
          .toList()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeWith(new ResourceSingleObserver<List<Music>>() {
            @Override public void onSuccess(List<Music> musicList) {
              view().showAllSongs(musicList);
            }

            @Override public void onError(Throwable e) {
            }
          }));
    }
  }

  @Override public void onExitSearchClick() {
    view().exitSearch();
  }

  @Override public void onShuffleMenuClick() {
    List<Music> musics = new ArrayList<>(songs);
    Collections.shuffle(musics);
    musicPlayer.addToNowPlaylist(musics);
    view().showMusicScreen();
  }

  @Override public void onAddToPlayListMenuClick() {
    view().showAddToPlayListDialog();
  }

  @Override public void onSortMenuClick() {
    view().showSortMusicScreen();
  }

  @Override public void onSortEvent(SortEvent sortEvent) {
    if (isSongListAvailable()) {
      new MusicGroupOrder(songs).by(sortEvent.sortBy());
      if (sortEvent.isSortInDescending()) {
        Collections.reverse(songs);
      }
      view().showAllSongs(songs);
    }
  }

  @Override public void onDeleteSelectedMusic() {
    view().percentage(0);
    view().showProgressLayout();
    List<Music> selectedMusicList = view().selectedMusicList();
    final int selectedCount = selectedMusicList.size();
    view().deletedOutOfText(0 + " / " + selectedCount);
    Observable.zip(Observable.fromIterable(selectedMusicList),
        Observable.interval(25, TimeUnit.MILLISECONDS), new BiFunction<Music, Long, Music>() {
          @Override public Music apply(Music sqlMusic, Long aLong) throws Exception {
            return sqlMusic;
          }
        })
        .doOnNext(new Consumer<Music>() {
          @Override public void accept(Music music) throws Exception {
            String path = music.media().path();
            new File(path).delete();
            androidMediaStoreDatabase.deleteAndBroadcastDeletion(path);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(new Consumer<Music>() {
          int count = 0;

          @Override public void accept(Music music) throws Exception {
            ++count;
            int position = songs.indexOf(music);
            if (position == -1) {
              Crashlytics.logException(
                  new IllegalStateException("How songs.indexOf returning -1?"));
            } else {
              view().notifyItemRemoved(position);
              songs.remove(music);
            }
            view().percentage((int) (count / (float) selectedCount * 100));
            view().deletedOutOfText(count + " / " + selectedCount);
          }
        })
        .delay(25, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnTerminate(new Action() {
          @Override public void run() throws Exception {
            view().showMessage(String.format("%d songs deleted successfully!", selectedCount));
            view().clearSelection();
            view().hideSelectionContextOptions();
            view().refreshSongCount(songs.size());
            view().showAllSongsLayout();
          }
        })
        .subscribe();
  }

  @Override public void onSearchClick() {
    view().enterSearch();
  }
}
