package com.kingbull.musicplayer.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.appcompat.widget.AppCompatDrawableManager;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.table.AlbumTable;
import com.kingbull.musicplayer.image.GlideApp;
import com.kingbull.musicplayer.image.GlideBitmapPool;
import com.kingbull.musicplayer.ui.base.Image;
import com.kingbull.musicplayer.ui.main.MainActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Represents Notification of music player.
 *
 * @author Kailash Dabhi
 * @date 05 May, 2017 11:29 PM
 */
public final class MusicNotification {

  private static final int NOTIFICATION_ID = 1;
  private final Service context;
  private final AlbumTable albumTable = new AlbumTable();
  private final MediaSessionCompat mediaSessionCompat;
  private final com.kingbull.musicplayer.player.Player musicPlayer;
  private final BroadcastActionNames broadcastActionNames = new BroadcastActionNames();
  private RemoteViews bigRemoteView;
  private RemoteViews smallRemoteView;
  private Bitmap currentLockScreenBitmap;

  /**
   *
   */
  public MusicNotification(Service context, Player musicPlayer,
      MediaSessionCompat mediaSessionCompat) {
    this.context = context;
    this.musicPlayer = musicPlayer;
    this.mediaSessionCompat = mediaSessionCompat;
  }

  private RemoteViews smallRemoteView() {
    if (smallRemoteView == null) {
      smallRemoteView =
          new RemoteViews(context.getPackageName(), R.layout.remote_view_music_player_small);
      setUpRemoteView(smallRemoteView);
    }
    return smallRemoteView;
  }

  private void setUpRemoteView(RemoteViews remoteView) {
    setVectorDrawable(remoteView, R.id.image_view_close, R.drawable.ic_remote_view_close);
    setVectorDrawable(remoteView, R.id.image_view_play_last, R.drawable.ic_remote_view_play_last);
    setVectorDrawable(remoteView, R.id.image_view_play_next, R.drawable.ic_remote_view_play_next);
    remoteView.setOnClickPendingIntent(R.id.button_close,
        getPendingIntent(broadcastActionNames.ofStop()));
    remoteView.setOnClickPendingIntent(R.id.button_play_last,
        getPendingIntent(broadcastActionNames.ofPlayLast()));
    remoteView.setOnClickPendingIntent(R.id.button_play_next,
        getPendingIntent(broadcastActionNames.ofPlayNext()));
    remoteView.setOnClickPendingIntent(R.id.button_play_toggle,
        getPendingIntent(broadcastActionNames.ofPlayToggle()));
  }

  private void setVectorDrawable(RemoteViews remoteViews, @IdRes int viewId,
      @DrawableRes int srcId) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      remoteViews.setImageViewResource(viewId, srcId);
    } else {
      Bitmap bitmap = BitmapMemoryCache.instance().get(String.valueOf(srcId));
      if (bitmap == null) {
        Drawable drawable = ContextCompat.getDrawable(context, srcId);
        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        BitmapMemoryCache.instance().add(String.valueOf(srcId), bitmap);
      }
      remoteViews.setImageViewBitmap(viewId, bitmap);
    }
  }

  private PendingIntent getPendingIntent(String action) {
    return PendingIntent.getService(context, 0, new Intent(action), PendingIntent.FLAG_IMMUTABLE);
  }

  private RemoteViews bigRemoteView() {
    if (bigRemoteView == null) {
      bigRemoteView = new RemoteViews(context.getPackageName(), R.layout.remote_view_music_player);
      setUpRemoteView(bigRemoteView);
    }
    return bigRemoteView;
  }

  /**
   * Display notification for music player.
   */
  public void show() {
    Observable.just(musicPlayer.getPlayingSong())
        .subscribeOn(Schedulers.io())
        .flatMap((Function<Music, ObservableSource<Pair<Music, String>>>) music -> {
          String albumArt = albumTable.albumById(music.media().albumId()).albumArt();
          return Observable.just(new Pair<>(music, albumArt));
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<Pair<Music, String>>() {
          @Override public void onNext(final Pair<Music, String> value) {
            GlideApp.with(context)
                .asBitmap()
                .load(value.second)
                .error(R.drawable.bass_guitar)
                .into(new SimpleTarget<Bitmap>(205, 205) {
                  @Override public void onResourceReady(@NonNull Bitmap resource,
                      @Nullable Transition<? super Bitmap> transition) {
                    updateNotification(new Pair<Music, Bitmap>(value.first, resource));
                  }

                  @Override public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    updateNotification(new Pair<Music, Bitmap>(value.first,
                        ((BitmapDrawable) errorDrawable).getBitmap()));
                  }
                });
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onComplete() {
          }
        });
  }

  private synchronized void updateNotification(Pair<Music, Bitmap> pair) {
    Music music = pair.first;
    Bitmap album = pair.second;
    updateRemoteViews(smallRemoteView(), music.media(), album);
    updateRemoteViews(bigRemoteView(), music.media(), album);
    if (currentLockScreenBitmap != null) {
      GlideBitmapPool.instance().put(currentLockScreenBitmap);
    }
    currentLockScreenBitmap =
        new Image.Smart(album)
            .blurred(25)
            .saturated()
            .bitmap();
    updateMediaSessionMetaData(music.media(), currentLockScreenBitmap);
    // The PendingIntent to launch our activity if the user selects this notification
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra("from", "notification");
    PendingIntent contentIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    String channelId = "";
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      channelId = notificationChannel();
    }
    Notification notification = new NotificationCompat.Builder(context, channelId).setSmallIcon(
            R.drawable.ic_notification_app_logo)  // the status icon
        .setWhen(System.currentTimeMillis())  // the time stamp
        .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
        .setCustomContentView(smallRemoteView())
        .setCustomBigContentView(bigRemoteView())
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setOngoing(true)
        .build();
    // Send the notification.
    context.startForeground(NOTIFICATION_ID, notification);
  }

  @RequiresApi(api = Build.VERSION_CODES.O) private String notificationChannel() {
    String channelId = "com.kingbull.musicplayer.musicnotification.channel";
    String channelName = "com.kingbull.musicplayer.player.MusicService";
    NotificationChannel channel = new NotificationChannel(channelId,
        channelName, NotificationManagerCompat.IMPORTANCE_NONE);
    channel.setLightColor(Color.BLUE);
    channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
    NotificationManager service =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    service.createNotificationChannel(channel);
    return channelId;
  }

  private void updateRemoteViews(final RemoteViews remoteView, Media media, Bitmap album) {
    remoteView.setTextViewText(R.id.nameTextView, media.title());
    remoteView.setTextViewText(R.id.text_view_artist, media.artist());
    setVectorDrawable(remoteView, R.id.image_view_play_toggle,
        musicPlayer.isPlaying() ? R.drawable.ic_remote_view_pause : R.drawable.ic_remote_view_play);
    remoteView.setImageViewBitmap(R.id.albumImageView, album);
  }

  private void updateMediaSessionMetaData(Media media, Bitmap bitmap) {
    mediaSessionCompat.setMetadata(
        new MediaMetadataCompat.Builder().putString(MediaMetadataCompat.METADATA_KEY_ARTIST,
                media.artist())
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, media.album())
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, media.title())
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, media.duration())
            .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
            .build());
  }
}
