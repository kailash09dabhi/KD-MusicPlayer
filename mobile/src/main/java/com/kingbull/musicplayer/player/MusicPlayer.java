package com.kingbull.musicplayer.player;

import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.Log;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import com.kingbull.musicplayer.event.MusicEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public final class MusicPlayer implements Player, MediaPlayer.OnCompletionListener {
  private static final String TAG = MusicPlayer.class.getSimpleName();

  private MediaPlayer player;
  private NowPlayingList nowPlayingList;
  // Default size 2: for service and UI
  private List<Callback> mCallbacks = new ArrayList<>(2);
  private boolean isPaused;

  @Inject public MusicPlayer() {
    player = new MediaPlayer();
    nowPlayingList = new NowPlayingList.Smart();
    player.setOnCompletionListener(this);
  }

  @Override public boolean play() {
    if (isPaused) {
      player.start();
      notifyPlayStatusChanged(true);
    } else {
      Music song = nowPlayingList.currentMusic();
      try {
        player.reset();
        player.setDataSource(song.path());
        player.prepare();
        player.start();
        notifyPlayStatusChanged(true);
        ((SqlMusic) song).save();
      } catch (IOException e) {
        Log.e(TAG, "play: ", e);
        notifyPlayStatusChanged(false);
        return false;
      }
    }
    isPaused = false;
    return true;
  }

  @Override public boolean play(Music music) {
    nowPlayingList.jumpTo(music);
    play();
    RxBus.getInstance().post(new MusicEvent(nowPlayingList.currentMusic(),MusicPlayerEvent.PLAY));
    return true;
  }

  @Override public boolean playLast() {
    isPaused = false;
    boolean hasLast = nowPlayingList.size() > 0;
    if (hasLast) {
      Music last = nowPlayingList.get(nowPlayingList.size() - 1);
      play();
      notifyPlayLast(last);
      return true;
    }
    return false;
  }

  @Override public boolean playNext() {
    isPaused = false;
    boolean hasNext = nowPlayingList.size() > nowPlayingList.indexOf(nowPlayingList.currentMusic());
    if (hasNext) {
      Music next = nowPlayingList.next();
      play();
      notifyPlayNext(next);
      RxBus.getInstance().post(new MusicEvent(next,MusicPlayerEvent.NEXT));
      return true;
    }
    return false;
  }

  @Override public boolean pause() {
    if (player.isPlaying()) {
      player.pause();
      isPaused = true;
      notifyPlayStatusChanged(false);
      RxBus.getInstance().post(new MusicEvent(nowPlayingList.currentMusic(),MusicPlayerEvent.PAUSE));
      return true;
    }
    return false;
  }

  @Override public boolean isPlaying() {
    return player.isPlaying();
  }

  @Override public int getProgress() {
    return player.getCurrentPosition();
  }

  @Nullable @Override public Music getPlayingSong() {
    return nowPlayingList.currentMusic();
  }

  @Override public boolean seekTo(int progress) {
    if (nowPlayingList.isEmpty()) return false;
    Music currentSong = nowPlayingList.currentMusic();
    if (currentSong != null) {
      if (currentSong.duration() <= progress) {
        onCompletion(player);
      } else {
        player.seekTo(progress);
      }
      return true;
    }
    return false;
  }

  @Override public void setPlayMode(PlayMode playMode) {
    nowPlayingList.useMusicMode(playMode);
  }
  // Listeners

  @Override public void onCompletion(MediaPlayer mp) {
    Music next = nowPlayingList.next();
    if (next != null) play();
    notifyComplete(next);
  }

  @Override public void releasePlayer() {
    nowPlayingList = null;
    player.reset();
    player.release();
    player = null;
  }

  @Override public void addToNowPlaylist(List<Music> songs) {
    nowPlayingList.clear();
    nowPlayingList.addAll(songs);
  }

  @Override public NowPlayingList nowPlayingMusicList() {
    return nowPlayingList;
  }

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

  public int audioSessionId() {
    return player.getAudioSessionId();
  }
}
