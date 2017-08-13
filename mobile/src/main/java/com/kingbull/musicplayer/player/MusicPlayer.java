package com.kingbull.musicplayer.player;

import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Virtualizer;
import android.support.annotation.Nullable;
import com.crashlytics.android.Crashlytics;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.Time;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.event.MusicEvent;
import com.kingbull.musicplayer.ui.equalizer.reverb.Reverb;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public final class MusicPlayer implements Player, MediaPlayer.OnCompletionListener {
  private static final String TAG = MusicPlayer.class.getSimpleName();
  private final SettingPreferences settingPrefs;
  private final AudioFocus audioFocus;
  private final int priority = 0;
  private boolean isAudioSessionIdUpdated = false;
  private BassBoost bassBoost;
  private Virtualizer virtualizer;
  private Time time;
  private MediaPlayer player;
  private NowPlayingList nowPlayingList;
  private boolean isPaused;
  private android.media.audiofx.Equalizer equalizer;
  private boolean isMusicChanged = false;

  public MusicPlayer(SettingPreferences settingPreferences) {
    player = new MediaPlayer();
    settingPrefs = settingPreferences;
    nowPlayingList = new NowPlayingList.Smart();
    player.setOnCompletionListener(this);
    audioFocus = new AudioFocus(MusicPlayerApp.instance(), new AudioFocusListener(this));
  }

  @Override public void onCompletion(MediaPlayer mp) {
    if (!nowPlayingList.isEmpty()) {
      RxBus.getInstance()
          .post(new MusicEvent(nowPlayingList.currentMusic(), MusicPlayerEvent.COMPLETED));
      switch (settingPrefs.musicMode()) {
        case REPEAT_ALL:
          playNext();
          break;
        case REPEAT_SINGLE:
          play();
          break;
        case REPEAT_NONE:
          if (nowPlayingList.indexOf(nowPlayingList.currentMusic()) < nowPlayingList.size() - 1) {
            playNext();
          }
          break;
      }
    }
  }

  @Override public boolean play() {
    if (nowPlayingList.isEmpty()) {
      return false;
    }
    audioFocus.requestFocus();
    if (isPaused && !isMusicChanged) {
      time = new Time.Now();
      player.start();
    } else {
      Music music = nowPlayingList.currentMusic();
      isMusicChanged = false;
      try {
        player.reset();
        player.setDataSource(music.media().path());
        player.prepare();
        player.start();
        isAudioSessionIdUpdated = true;
        time = new Time.Now();
        music.mediaStat().saveLastPlayed();
      } catch (IOException e) {
        Crashlytics.logException(new IOException(
            "play() got IOException! " + e.getMessage() + " is File readable? " + new File(
                music.media().path()).canRead(), e));
        try {
          FileInputStream fis = new FileInputStream(music.media().path());
          player.setDataSource(fis.getFD());
          player.reset();
          player.setDataSource(music.media().path());
          player.prepare();
          player.start();
          isAudioSessionIdUpdated = true;
          time = new Time.Now();
          music.mediaStat().saveLastPlayed();
        } catch (FileNotFoundException e1) {
          Crashlytics.logException(new IllegalStateException(
              "play() catch(FileNotFoundException){}  parent: " + e + "child:" + e1, e1));
          return false;
        } catch (IOException e1) {
          Crashlytics.logException(
              new IOException("play() catch(IOException)  parent: " + e + "child:" + e1, e1));
          return false;
        } catch (IllegalStateException e1) {
          Crashlytics.logException(new IllegalStateException(
              "play() catch(IllegalStateException){}  parent: " + e + "child:" + e1, e1));
          return false;
        }
        return true;
      } catch (IllegalStateException e) {
        Crashlytics.logException(
            new IllegalStateException("play() got IllegalStateException! " + e.getMessage(), e));
        return false;
      }
    }
    RxBus.getInstance().post(new MusicEvent(nowPlayingList.currentMusic(), MusicPlayerEvent.PLAY));
    isPaused = false;
    return true;
  }

  @Override public boolean play(Music music) {
    if (!nowPlayingList.isEmpty()) {
      if (time != null) {
        nowPlayingList.currentMusic().mediaStat()
            .addToListenedTime(time.difference(new Time.Now()));
      }
      nowPlayingList.jumpTo(music);
      isMusicChanged = true;
      play();
      return true;
    } else {
      return false;
    }
  }

  @Override public boolean playPrevious() {
    if (!nowPlayingList.isEmpty()) {
      if (time != null) {
        nowPlayingList.currentMusic()
            .mediaStat()
            .addToListenedTime(time.difference(new Time.Now()));
      }
      RxBus.getInstance()
          .post(new MusicEvent(nowPlayingList.previous(), MusicPlayerEvent.PREVIOUS));
      play();
      return true;
    }
    return false;
  }

  @Override public boolean playNext() {
    if (!nowPlayingList.isEmpty()) {
      if (time != null) {
        nowPlayingList.currentMusic()
            .mediaStat()
            .addToListenedTime(time.difference(new Time.Now()));
      }
      RxBus.getInstance().post(new MusicEvent(nowPlayingList.next(), MusicPlayerEvent.NEXT));
      play();
      return true;
    }
    return false;
  }

  @Override public boolean pause() {
    if (player.isPlaying()) {
      player.pause();
      isPaused = true;
      if (time != null) {
        nowPlayingList.currentMusic()
            .mediaStat()
            .addToListenedTime(time.difference(new Time.Now()));
      }
      RxBus.getInstance()
          .post(new MusicEvent(nowPlayingList.currentMusic(), MusicPlayerEvent.PAUSE));
      return true;
    }
    return false;
  }

  @Override public boolean isPlaying() {
    return player.isPlaying();
  }

  @Override public void setVolume(float volume) {
    player.setVolume(volume, volume);
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

  @Override public boolean seekTo(int millSeconds) {
    if (nowPlayingList.isEmpty()) {
      return false;
    }
    Music currentSong = nowPlayingList.currentMusic();
    if (currentSong != null) {
      player.seekTo(millSeconds);
      return true;
    }
    return false;
  }

  @Override public void useEffect(Reverb reverb) {
    try {
      PresetReverb presetReverb = new PresetReverb(priority, player.getAudioSessionId());
      presetReverb.setPreset(reverb.id());
      presetReverb.setEnabled(true);
      player.setAuxEffectSendLevel(1.0f);
    } catch (IllegalArgumentException | IllegalStateException | UnsupportedOperationException e) {
      Crashlytics.logException(e);
    }
  }

  @Override public BassBoost bassBoost() {
    if (isAudioSessionIdUpdated || bassBoost == null) {
      bassBoost = new android.media.audiofx.BassBoost(priority, player.getAudioSessionId());
      bassBoost.setEnabled(true);
    }
    return bassBoost;
  }

  @Override public Virtualizer virtualizer() {
    if (isAudioSessionIdUpdated || virtualizer == null) {
      virtualizer = new android.media.audiofx.Virtualizer(priority, player.getAudioSessionId());
      virtualizer.setEnabled(true);
    }
    return virtualizer;
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
}
