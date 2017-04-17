package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
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
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import org.reactivestreams.Publisher;

/**
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
                AlbumPresenter.this.songs = musicList;
                view().showSongs(musicList);
                compositeDisposable.add(Flowable.fromIterable(musicList)
                    .flatMap(new Function<Music, Publisher<Long>>() {
                      @Override public Publisher<Long> apply(Music music) throws Exception {
                        return Flowable.just(music.media().duration());
                      }
                    })
                    .toList()
                    .map(new Function<List<Long>, Long>() {
                      @Override public Long apply(List<Long> longs) throws Exception {
                        long sum = 0L;
                        for (long lng : longs)
                          sum = sum + lng;
                        return sum;
                      }
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
    if (!sortEvent.isSortInAscending()) Collections.reverse(songs);
    view().showSongs(songs);
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

  @Override public void onClearSelectionClick() {
    view().clearSelection();
  }
}
