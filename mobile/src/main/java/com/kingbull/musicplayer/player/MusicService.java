package com.kingbull.musicplayer.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.widget.RemoteViews;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.event.MusicEvent;
import com.kingbull.musicplayer.ui.equalizer.reverb.Reverb;
import com.kingbull.musicplayer.ui.main.MainActivity;
import com.kingbull.musicplayer.utils.AlbumUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import java.util.List;
import javax.inject.Inject;

import static com.kingbull.musicplayer.R.id.albumImageView;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/12/16
 * Time: 4:27 PM
 * Desc: PlayService
 */
public final class MusicService extends Service implements Player {

  public static final String ACTION_STOP_SERVICE = "com.kingbull.musicplayer.ACTION_STOP_SERVICE";
  private static final String ACTION_PLAY_TOGGLE = "com.kingbull.musicplayer.ACTION_PLAY_TOGGLE";
  private static final String ACTION_PLAY_LAST = "com.kingbull.musicplayer.ACTION_PLAY_LAST";
  private static final String ACTION_PLAY_NEXT = "com.kingbull.musicplayer.ACTION_PLAY_NEXT";
  private static final int NOTIFICATION_ID = 1;
  private final Binder mBinder = new LocalBinder();
  @Inject Player musicPlayer;
  private RemoteViews mContentViewBig, mContentViewSmall;
  private MediaSessionCompat mediaSession;
  private CompositeDisposable compositeDisposable;

  @Override public void onCreate() {
    super.onCreate();
    MusicPlayerApp.instance().component().inject(this);
    compositeDisposable = new CompositeDisposable();
    compositeDisposable.add(RxBus.getInstance()
        .toObservable()
        .ofType(MusicEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<MusicEvent>() {
          @Override public void onNext(MusicEvent musicEvent) {
            showNotification();
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

  @Nullable @Override public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override public boolean stopService(Intent name) {
    stopForeground(true);
    return super.stopService(name);
  }

  @Override public void onDestroy() {
    releasePlayer();
    if (compositeDisposable != null) compositeDisposable.clear();
    super.onDestroy();
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
    super.onDestroy();
  }

  @Override public void addToNowPlaylist(List<Music> songs) {
    musicPlayer.addToNowPlaylist(songs);
  }

  @Override public NowPlayingList nowPlayingMusicList() {
    return musicPlayer.nowPlayingMusicList();
  }

  /**
   * Show a notification while this service is running.
   */
  private void showNotification() {
    // The PendingIntent to launch our activity if the user selects this notification
    PendingIntent contentIntent =
        PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
    // Set the info for the views that show in the notification panel.
    Notification notification = new NotificationCompat.Builder(this).setSmallIcon(
        R.drawable.ic_notification_app_logo)  // the status icon
        .setWhen(System.currentTimeMillis())  // the time stamp
        .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
        .setCustomContentView(getSmallContentView())
        .setCustomBigContentView(getBigContentView())
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setOngoing(true)
        .build();
    // Send the notification.
    startForeground(NOTIFICATION_ID, notification);
  }

  private RemoteViews getSmallContentView() {
    if (mContentViewSmall == null) {
      mContentViewSmall =
          new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
      setUpRemoteView(mContentViewSmall);
    }
    updateRemoteViews(mContentViewSmall);
    return mContentViewSmall;
  }

  private RemoteViews getBigContentView() {
    if (mContentViewBig == null) {
      mContentViewBig = new RemoteViews(getPackageName(), R.layout.remote_view_music_player);
      setUpRemoteView(mContentViewBig);
    }
    updateRemoteViews(mContentViewBig);
    return mContentViewBig;
  }

  private void setUpRemoteView(RemoteViews remoteView) {
    setVectorDrawable(remoteView, R.id.image_view_close, R.drawable.ic_remote_view_close);
    setVectorDrawable(remoteView, R.id.image_view_play_last, R.drawable.ic_remote_view_play_last);
    setVectorDrawable(remoteView, R.id.image_view_play_next, R.drawable.ic_remote_view_play_next);
    remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(ACTION_STOP_SERVICE));
    remoteView.setOnClickPendingIntent(R.id.button_play_last, getPendingIntent(ACTION_PLAY_LAST));
    remoteView.setOnClickPendingIntent(R.id.button_play_next, getPendingIntent(ACTION_PLAY_NEXT));
    remoteView.setOnClickPendingIntent(R.id.button_play_toggle,
        getPendingIntent(ACTION_PLAY_TOGGLE));
  }

  private void setVectorDrawable(RemoteViews remoteViews, @IdRes int resId,
      @DrawableRes int drawableId) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      remoteViews.setImageViewResource(resId, drawableId);
    } else {
      Drawable d = AppCompatDrawableManager.get().getDrawable(this, drawableId);
      Bitmap b = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(),
          Bitmap.Config.ARGB_8888);
      Canvas c = new Canvas(b);
      d.setBounds(0, 0, c.getWidth(), c.getHeight());
      d.draw(c);
      remoteViews.setImageViewBitmap(resId, b);
    }
  }

  private void updateRemoteViews(RemoteViews remoteView) {
    Music music = musicPlayer.getPlayingSong();
    if (music != null) {
      remoteView.setTextViewText(R.id.nameTextView, music.media().title());
      remoteView.setTextViewText(R.id.text_view_artist, music.media().artist());
    }
    setVectorDrawable(remoteView, R.id.image_view_play_toggle,
        isPlaying() ? R.drawable.ic_remote_view_pause : R.drawable.ic_remote_view_play);
    Bitmap album = AlbumUtils.parseAlbum(getPlayingSong());
    if (album == null) {
      remoteView.setImageViewResource(albumImageView, R.mipmap.ic_launcher);
    } else {
      remoteView.setImageViewBitmap(albumImageView, album);
    }
    mediaSession.setMetadata(
        new MediaMetadataCompat.Builder().putString(MediaMetadataCompat.METADATA_KEY_ARTIST,
            music.media().artist())
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, music.media().album())
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.media().title())
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 10000)
            .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, album)
            .build());
  }

  private PendingIntent getPendingIntent(String action) {
    return PendingIntent.getService(this, 0, new Intent(action), 0);
  }
  // PendingIntent

  public class LocalBinder extends Binder {
    public MusicService getService() {
      return MusicService.this;
    }
  }
}
