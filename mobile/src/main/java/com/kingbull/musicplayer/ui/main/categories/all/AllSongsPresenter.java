package com.kingbull.musicplayer.ui.main.categories.all;

import android.database.Cursor;
import android.util.Log;
import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import static android.content.ContentValues.TAG;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */
public final class AllSongsPresenter extends Presenter<AllSongs.View>
    implements AllSongs.Presenter {
  private final AndroidMediaStoreDatabase androidMediaStoreDatabase =
      new AndroidMediaStoreDatabase();
  @Inject Player musicPlayer;
  private List<Music> songs;

  @Override public void onAllSongsCursorLoadFinished(Cursor cursor) {
    if (cursor != null && cursor.getCount() > 0) {
      if (cursor.isClosed()) {
        musicPlayer.addToNowPlaylist(songs);
        view().showAllSongs(songs);
      } else {
        compositeDisposable.add(
            Flowable.just(cursor)
                .flatMap(new Function<Cursor, Flowable<List<Music>>>() {
                  @Override public Flowable<List<Music>> apply(Cursor cursor) {
                    List<Music> songs = new ArrayList<>();
                    cursor.moveToFirst();
                    do {
                      Music music = new SqlMusic(new Media.Smart(cursor));
                      if (!new File(music.media().path()).exists()) {
                        androidMediaStoreDatabase.deleteAndBroadcastDeletion(music.media().path());
                      } else {
                        songs.add(music);
                      }
                    } while (cursor.moveToNext());
                    return Flowable.just(songs);
                  }
                })
                .doOnNext(new Consumer<List<Music>>() {
                  @Override public void accept(List<Music> songs) {
                    Log.d(TAG, "onLoadFinished: " + songs.size());
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
                  @Override public void onNext(List<Music> songs) {
                    AllSongsPresenter.this.songs = songs;
                    musicPlayer.addToNowPlaylist(songs);
                    view().showAllSongs(songs);
                  }

                  @Override public void onError(Throwable throwable) {
                    Log.e(TAG, "onError: ", throwable);
                  }

                  @Override public void onComplete() {
                  }
                }));
      }
    }
  }

  @Override public void onSearchTextChanged(final String text) {
    compositeDisposable.add(Flowable.fromIterable(songs).subscribeOn(Schedulers.computation())
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
    switch (sortEvent.sortBy()) {
      case SortEvent.SortBy.TITLE:
        Collections.sort(songs, new Comparator<Music>() {
          @Override public int compare(Music song1, Music song2) {
            return song1.media().title().compareTo(song2.media().title());
          }
        });
        break;
      case SortEvent.SortBy.ARTIST:
        Collections.sort(songs, new Comparator<Music>() {
          @Override public int compare(Music song1, Music song2) {
            return song1.media().artist().compareTo(song2.media().artist());
          }
        });
        break;
      case SortEvent.SortBy.ALBUM:
        Collections.sort(songs, new Comparator<Music>() {
          @Override public int compare(Music song1, Music song2) {
            return song1.media().album().compareTo(song2.media().album());
          }
        });
        break;
      case SortEvent.SortBy.DURATION:
        Collections.sort(songs, new Comparator<Music>() {
          @Override public int compare(Music song1, Music song2) {
            long durationSong1 = song1.media().duration();
            long durationSong2 = song2.media().duration();
            if (durationSong1 < durationSong2) {
              return 1;
            } else if (durationSong1 > durationSong2) {
              return -1;
            } else {
              return 0;
            }
          }
        });
        break;
      case SortEvent.SortBy.DATE_ADDED:
        Collections.sort(songs, new Comparator<Music>() {
          @Override public int compare(Music song1, Music song2) {
            long dateAddedSong1 = song1.media().dateAdded();
            long dateAddedSong2 = song2.media().dateAdded();
            if (dateAddedSong1 < dateAddedSong2) {
              return 1;
            } else if (dateAddedSong1 > dateAddedSong2) {
              return -1;
            } else {
              return 0;
            }
          }
        });
        break;
      case SortEvent.SortBy.YEAR:
        Collections.sort(songs, new Comparator<Music>() {
          @Override public int compare(Music song1, Music song2) {
            long yearSong1 = song1.media().year();
            long yearSong2 = song2.media().year();
            if (yearSong1 < yearSong2) {
              return 1;
            } else if (yearSong1 > yearSong2) {
              return -1;
            } else {
              return 0;
            }
          }
        });
        break;
    }
    if (sortEvent.isSortInDescending()) Collections.reverse(songs);
    view().showAllSongs(songs);
  }

  @Override public void onDeleteSelectedMusic() {
    view().percentage(0);
    view().showProgressLayout();
    List<SqlMusic> selectedMusicList = view().selectedMusicList();
    final int selectedCount = selectedMusicList.size();
    view().deletedOutOfText(0 + " / " + selectedCount);
    Observable.zip(Observable.fromIterable(selectedMusicList),
        Observable.interval(25, TimeUnit.MILLISECONDS), new BiFunction<SqlMusic, Long, SqlMusic>() {
          @Override public SqlMusic apply(SqlMusic sqlMusic, Long aLong) throws Exception {
            return sqlMusic;
          }
        })
        .doOnNext(new Consumer<SqlMusic>() {
          @Override public void accept(SqlMusic music) throws Exception {
            String path = music.media().path();
            new File(path).delete();
            androidMediaStoreDatabase.deleteAndBroadcastDeletion(path);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(new Consumer<SqlMusic>() {
          int count = 0;

          @Override public void accept(SqlMusic music) throws Exception {
            ++count;
            view().notifyItemRemoved(songs.indexOf(music));
            songs.remove(music);
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
