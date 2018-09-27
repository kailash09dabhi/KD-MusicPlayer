package com.kingbull.musicplayer.player;

import android.app.Service;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import com.bumptech.glide.Glide;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.event.MusicEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import javax.inject.Inject;

public final class MusicService extends Service {
  private final BroadcastActionNames broadcastActionNames = new BroadcastActionNames();
  private final Binder mBinder = new LocalBinder();
  private final HeadsetPlugReceiver headsetPlugReceiver = new HeadsetPlugReceiver();
  private final ComponentCallbacks2 componentCallbacks2 = new ComponentCallbacks2() {
    @Override public void onTrimMemory(int level) {
      Glide.get(MusicService.this).onTrimMemory(level);
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override public void onLowMemory() {
      Glide.get(MusicService.this).onLowMemory();
    }
  };
  @Inject Player musicPlayer;
  private MediaSessionCompat mediaSession;
  private CompositeDisposable compositeDisposable;
  private MusicNotification musicNotification;

  @Override public void onCreate() {
    super.onCreate();
    MusicPlayerApp.instance().component().inject(this);
    registerReceiver(headsetPlugReceiver, headsetPlugReceiver.intentFilter());
    registerComponentCallbacks(componentCallbacks2);
    compositeDisposable = new CompositeDisposable();
    compositeDisposable
        .add(MusicEventRelay.instance().asObservable().observeOn(AndroidSchedulers.mainThread())
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
      if (broadcastActionNames.ofPlayToggle().equals(action)) {
        if (musicPlayer.isPlaying()) {
          musicPlayer.pause();
        } else {
          musicPlayer.play();
        }
      } else if (broadcastActionNames.ofPlayNext().equals(action)) {
        musicPlayer.playNext();
      } else if (broadcastActionNames.ofPlayLast().equals(action)) {
        musicPlayer.playPrevious();
      } else if (broadcastActionNames.ofStop().equals(action)) {
        if (musicPlayer.isPlaying()) {
          musicPlayer.pause();
        }
        stopForeground(true);
      }
    }
    return START_STICKY;
  }

  @Override public void onDestroy() {
    if (compositeDisposable != null) {
      compositeDisposable.clear();
    }
    unregisterReceiver(headsetPlugReceiver);
    unregisterComponentCallbacks(componentCallbacks2);
    super.onDestroy();
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return mBinder;
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
