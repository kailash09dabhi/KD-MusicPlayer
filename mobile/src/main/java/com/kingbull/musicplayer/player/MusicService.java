package com.kingbull.musicplayer.player;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.event.MusicEvent;
import com.kingbull.musicplayer.ui.equalizer.reverb.Reverb;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public final class MusicService extends Service implements Player {
  public static final String ACTION_STOP_SERVICE = "com.kingbull.kdmusicplayer.ACTION_STOP_SERVICE";
  public static final String ACTION_PLAY_TOGGLE = "com.kingbull.kdmusicplayer.ACTION_PLAY_TOGGLE";
  public static final String ACTION_PLAY_LAST = "com.kingbull.kdmusicplayer.ACTION_PLAY_LAST";
  public static final String ACTION_PLAY_NEXT = "com.kingbull.kdmusicplayer.ACTION_PLAY_NEXT";
  private final Binder mBinder = new LocalBinder();
  private final HeadsetPlugReceiver headsetPlugReceiver = new HeadsetPlugReceiver();
  @Inject Player musicPlayer;
  private MediaSessionCompat mediaSession;
  private CompositeDisposable compositeDisposable;
  private MusicNotification musicNotification;

  @Override public void onCreate() {
    super.onCreate();
    MusicPlayerApp.instance().component().inject(this);
    registerReceiver(headsetPlugReceiver, headsetPlugReceiver.intentFilter());
    compositeDisposable = new CompositeDisposable();
    compositeDisposable.add(RxBus.getInstance()
        .toObservable()
        .ofType(MusicEvent.class)
        .debounce(151, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<MusicEvent>() {
          @Override public void onNext(MusicEvent musicEvent) {
            musicNotification.show();
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onComplete() {
          }
        }));
    bindService(new Intent(this, MusicService.class), new ServiceConnection() {
      @Override public void onServiceConnected(ComponentName name, IBinder service) {
      }

      @Override public void onServiceDisconnected(ComponentName name) {
      }
    }, BIND_AUTO_CREATE);
    lockScreenMediaSessionSetup();
    musicNotification = new MusicNotification(this, musicPlayer, mediaSession);
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent != null) {
      String action = intent.getAction();
      if (ACTION_PLAY_TOGGLE.equals(action)) {
        if (isPlaying()) {
          pause();
        } else {
          play();
        }
      } else if (ACTION_PLAY_NEXT.equals(action)) {
        playNext();
      } else if (ACTION_PLAY_LAST.equals(action)) {
        playPrevious();
      } else if (ACTION_STOP_SERVICE.equals(action)) {
        if (isPlaying()) {
          pause();
        }
        stopForeground(true);
      }
    }
    return START_STICKY;
  }

  @Override public void onDestroy() {
    if (compositeDisposable != null) compositeDisposable.clear();
    unregisterReceiver(headsetPlugReceiver);
    super.onDestroy();
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override public boolean play() {
    return musicPlayer.play();
  }

  @Override public boolean play(Music music) {
    return musicPlayer.play(music);
  }

  @Override public boolean playPrevious() {
    return musicPlayer.playPrevious();
  }

  @Override public boolean playNext() {
    return musicPlayer.playNext();
  }

  @Override public boolean pause() {
    return musicPlayer.pause();
  }

  @Override public boolean isPlaying() {
    return musicPlayer.isPlaying();
  }

  @Override public int getProgress() {
    return musicPlayer.getProgress();
  }

  @Override public Music getPlayingSong() {
    return musicPlayer.getPlayingSong();
  }

  @Override public Equalizer equalizer() {
    return musicPlayer.equalizer();
  }

  @Override public boolean seekTo(int progress) {
    return musicPlayer.seekTo(progress);
  }

  @Override public void useEffect(Reverb reverb) {
    musicPlayer.useEffect(reverb);
  }

  @Override public BassBoost bassBoost() {
    return musicPlayer.bassBoost();
  }

  @Override public Virtualizer virtualizer() {
    return null;
  }

  @Override public void releasePlayer() {
    musicPlayer.releasePlayer();
  }

  @Override public void addToNowPlaylist(List<Music> songs) {
    musicPlayer.addToNowPlaylist(songs);
  }

  @Override public NowPlayingList nowPlayingMusicList() {
    return musicPlayer.nowPlayingMusicList();
  }

  private void lockScreenMediaSessionSetup() {
    ComponentName receiver = new ComponentName(getPackageName(), RemoteReceiver.class.getName());
    mediaSession = new MediaSessionCompat(this, "PlayerService", receiver, null);
    mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
        | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
    mediaSession.setPlaybackState(
        new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED, 0, 0)
            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
            .build());
    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
      @Override public void onAudioFocusChange(int focusChange) {
        // Ignore
      }
    }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    mediaSession.setActive(true);
  }

  @Override public boolean stopService(Intent name) {
    stopForeground(true);
    return super.stopService(name);
  }

  private class LocalBinder extends Binder {
    public MusicService getService() {
      return MusicService.this;
    }
  }
}
