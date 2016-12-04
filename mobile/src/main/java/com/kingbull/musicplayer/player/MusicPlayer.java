package com.kingbull.musicplayer.player;

import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Virtualizer;
import android.support.annotation.Nullable;
import android.util.Log;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.SettingPreferences;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import com.kingbull.musicplayer.event.MusicEvent;
import com.kingbull.musicplayer.ui.equalizer.reverb.Reverb;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public final class MusicPlayer implements Player, MediaPlayer.OnCompletionListener {
  private static final String TAG = MusicPlayer.class.getSimpleName();
  SettingPreferences settingPrefs = new SettingPreferences();
  boolean isAudioSessionIdUpdated = false;
  BassBoost bassBoost;
  Virtualizer virtualizer;
  private MediaPlayer player;
  private NowPlayingList nowPlayingList;
  // Default size 2: for service and UI
  private List<Callback> mCallbacks = new ArrayList<>(2);
  private boolean isPaused;
  private android.media.audiofx.Equalizer equalizer;

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
        player.setDataSource(song.media().path());
        player.prepare();
        player.start();
        isAudioSessionIdUpdated = true;
        notifyPlayStatusChanged(true);
        ((SqlMusic) song).mediaStat().saveLastPlayed();
      } catch (IOException e) {
        Log.e(TAG, "play: ", e);
        notifyPlayStatusChanged(false);
        return false;
      }
    }
    RxBus.getInstance().post(new MusicEvent(nowPlayingList.currentMusic(), MusicPlayerEvent.PLAY));
    isPaused = false;
    return true;
  }

  @Override public boolean play(Music music) {
    nowPlayingList.jumpTo(music);
    play();
    return true;
  }

  @Override public boolean playPrevious() {
    isPaused = false;
    nowPlayingList.previous();
    play();
    notifyPlayPrevious(nowPlayingList.currentMusic());
    RxBus.getInstance()
        .post(new MusicEvent(nowPlayingList.currentMusic(), MusicPlayerEvent.PREVIOUS));
    return true;
  }

  @Override public boolean playNext() {
    isPaused = false;
    boolean hasNext = nowPlayingList.size() > nowPlayingList.indexOf(nowPlayingList.currentMusic());
    if (hasNext) {
      Music next = nowPlayingList.next();
      play();
      notifyPlayNext(next);
      RxBus.getInstance().post(new MusicEvent(next, MusicPlayerEvent.NEXT));
      return true;
    }
    return false;
  }

  @Override public boolean pause() {
    if (player.isPlaying()) {
      player.pause();
      isPaused = true;
      notifyPlayStatusChanged(false);
      RxBus.getInstance()
          .post(new MusicEvent(nowPlayingList.currentMusic(), MusicPlayerEvent.PAUSE));
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

  @Override public android.media.audiofx.Equalizer equalizer() {
    if (isAudioSessionIdUpdated || equalizer == null) {
      equalizer = new android.media.audiofx.Equalizer(5, player.getAudioSessionId());
      equalizer.setEnabled(true);
    }
    return equalizer;
  }

  @Override public boolean seekTo(int progress) {
    if (nowPlayingList.isEmpty()) return false;
    Music currentSong = nowPlayingList.currentMusic();
    if (currentSong != null) {
      if (currentSong.media().duration() <= progress) {
        onCompletion(player);
      } else {
        player.seekTo(progress);
      }
      return true;
    }
    return false;
  }

  @Override public void useEffect(Reverb reverb) {
    PresetReverb mReverb = new PresetReverb(0, player.getAudioSessionId());//<<<<<<<<<<<<<
    mReverb.setPreset(reverb.id());
    mReverb.setEnabled(true);
    player.setAuxEffectSendLevel(1.0f);
  }

  @Override public BassBoost bassBoost() {
    if (isAudioSessionIdUpdated || bassBoost == null) {
      bassBoost = new android.media.audiofx.BassBoost(5, player.getAudioSessionId());
      bassBoost.setEnabled(true);
    }
    return bassBoost;
  }

  @Override public Virtualizer virtualizer() {
    if (isAudioSessionIdUpdated || virtualizer == null) {
      virtualizer = new android.media.audiofx.Virtualizer(5, player.getAudioSessionId());
      virtualizer.setEnabled(true);
    }
    return virtualizer;
  }

  @Override public void onCompletion(MediaPlayer mp) {
    switch (settingPrefs.musicMode()) {
      case REPEAT_ALL:
        if (nowPlayingList.indexOf(nowPlayingList.currentMusic()) >= nowPlayingList.size() - 1) {
          nowPlayingList.jumpTo(nowPlayingList.get(0));
        } else {
          nowPlayingList.next();
        }
        break;
      case REPEAT_SINGLE:
        break;
      case REPEAT_NONE:
        if (nowPlayingList.indexOf(nowPlayingList.currentMusic()) >= nowPlayingList.size() - 1) {
          nowPlayingList.jumpTo(null);
        } else {
          nowPlayingList.next();
        }
        break;
    }
    play();
    notifyComplete(nowPlayingList.currentMusic());
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

  private void notifyPlayPrevious(Music song) {
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
