package com.kingbull.musicplayer.player;

import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.Log;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/5/16
 * Time: 5:57 PM
 * Desc: Player
 */
public final class Player implements IPlayback, MediaPlayer.OnCompletionListener {

  private static final String TAG = "Player";

  private static volatile Player sInstance;

  private MediaPlayer mPlayer;

  private PlayList mPlayList;
  // Default size 2: for service and UI
  private List<Callback> mCallbacks = new ArrayList<>(2);

  // Player status
  private boolean isPaused;

  private Player() {
    mPlayer = new MediaPlayer();
    mPlayList = new PlayList();
    mPlayer.setOnCompletionListener(this);
  }

  public static Player getInstance() {
    if (sInstance == null) {
      synchronized (Player.class) {
        if (sInstance == null) {
          sInstance = new Player();
        }
      }
    }
    return sInstance;
  }

  @Override public void setPlayList(PlayList list) {
    if (list == null) {
      list = new PlayList();
    }
    mPlayList = list;
  }

  @Override public boolean play() {
    if (isPaused) {
      mPlayer.start();
      notifyPlayStatusChanged(true);
      return true;
    }
    if (mPlayList.prepare()) {
      Music song = mPlayList.getCurrentSong();
      try {
        mPlayer.reset();
        mPlayer.setDataSource(song.path());
        mPlayer.prepare();
        mPlayer.start();
        notifyPlayStatusChanged(true);
        new SqlMusic(song).save();


      } catch (IOException e) {
        Log.e(TAG, "play: ", e);
        notifyPlayStatusChanged(false);
        return false;
      }
      return true;
    }
    return false;
  }

  @Override public boolean play(PlayList list) {
    if (list == null) return false;
    isPaused = false;
    setPlayList(list);
    return play();
  }

  @Override public boolean play(PlayList list, int startIndex) {
    if (list == null || startIndex < 0 || startIndex >= list.getNumOfSongs()) return false;
    isPaused = false;
    list.setPlayingIndex(startIndex);
    setPlayList(list);
    return play();
  }

  @Override public boolean play(Music song) {
    if (song == null) return false;
    isPaused = false;
    mPlayList.getSongs().clear();
    mPlayList.getSongs().add(song);
    return play();
  }

  @Override public boolean playLast() {
    isPaused = false;
    boolean hasLast = mPlayList.hasLast();
    if (hasLast) {
      Music last = mPlayList.last();
      play();
      notifyPlayLast(last);
      return true;
    }
    return false;
  }

  @Override public boolean playNext() {
    isPaused = false;
    boolean hasNext = mPlayList.hasNext(false);
    if (hasNext) {
      Music next = mPlayList.next();
      play();
      notifyPlayNext(next);
      return true;
    }
    return false;
  }

  @Override public boolean pause() {
    if (mPlayer.isPlaying()) {
      mPlayer.pause();
      isPaused = true;
      notifyPlayStatusChanged(false);
      return true;
    }
    return false;
  }

  @Override public boolean isPlaying() {
    return mPlayer.isPlaying();
  }

  @Override public int getProgress() {
    return mPlayer.getCurrentPosition();
  }

  @Nullable @Override public Music getPlayingSong() {
    return mPlayList.getCurrentSong();
  }

  @Override public boolean seekTo(int progress) {
    if (mPlayList.getSongs().isEmpty()) return false;
    Music currentSong = mPlayList.getCurrentSong();
    if (currentSong != null) {
      if (currentSong.duration() <= progress) {
        onCompletion(mPlayer);
      } else {
        mPlayer.seekTo(progress);
      }
      return true;
    }
    return false;
  }

  @Override public void setPlayMode(PlayMode playMode) {
    mPlayList.setPlayMode(playMode);
  }
  // Listeners

  @Override public void onCompletion(MediaPlayer mp) {
    Music next = null;
    // There is only one limited play mode which is list, player should be stopped when hitting the list end
    if (mPlayList.getPlayMode() == PlayMode.LIST
        && mPlayList.getPlayingIndex() == mPlayList.getNumOfSongs() - 1) {
      // In the end of the list
      // Do nothing, just deliver the callback
    } else if (mPlayList.getPlayMode() == PlayMode.SINGLE) {
      next = mPlayList.getCurrentSong();
      play();
    } else {
      boolean hasNext = mPlayList.hasNext(true);
      if (hasNext) {
        next = mPlayList.next();
        play();
      }
    }
    notifyComplete(next);
  }

  @Override public void releasePlayer() {
    mPlayList = null;
    mPlayer.reset();
    mPlayer.release();
    mPlayer = null;
    sInstance = null;
  }
  // Callbacks

  @Override public void registerCallback(Callback callback) {
    mCallbacks.add(callback);
  }

  @Override public void unregisterCallback(Callback callback) {
    mCallbacks.remove(callback);
  }

  @Override public void removeCallbacks() {
    mCallbacks.clear();
  }

  private void notifyPlayStatusChanged(boolean isPlaying) {
    for (Callback callback : mCallbacks) {
      callback.onPlayStatusChanged(isPlaying);
    }
  }

  private void notifyPlayLast(Music song) {
    for (Callback callback : mCallbacks) {
      callback.onSwitchLast(song);
    }
  }

  private void notifyPlayNext(Music song) {
    for (Callback callback : mCallbacks) {
      callback.onSwitchNext(song);
    }
  }

  private void notifyComplete(Music song) {
    for (Callback callback : mCallbacks) {
      callback.onComplete(song);
    }
  }
}
