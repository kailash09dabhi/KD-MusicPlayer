package com.kingbull.musicplayer.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.widget.RemoteViews;
import com.bumptech.glide.Glide;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.table.AlbumTable;
import com.kingbull.musicplayer.ui.base.BitmapImage;
import com.kingbull.musicplayer.ui.main.MainActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.kingbull.musicplayer.player.MusicService.ACTION_PLAY_LAST;
import static com.kingbull.musicplayer.player.MusicService.ACTION_PLAY_NEXT;
import static com.kingbull.musicplayer.player.MusicService.ACTION_PLAY_TOGGLE;
import static com.kingbull.musicplayer.player.MusicService.ACTION_STOP_SERVICE;

/**
 * @author Kailash Dabhi
 * @date 05 May, 2017 11:29 PM
 */
public final class MusicNotification {
  private final int NOTIFICATION_ID = 1;
  private final Service context;
  private final AlbumTable albumTable = new AlbumTable();
  private final MediaSessionCompat mediaSessionCompat;
  private final com.kingbull.musicplayer.player.Player musicPlayer;
  private RemoteViews bigRemoteView;
  private RemoteViews smallRemoteView;

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
      Drawable d = AppCompatDrawableManager.get().getDrawable(context, drawableId);
      Bitmap b = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(),
          Bitmap.Config.ARGB_8888);
      Canvas c = new Canvas(b);
      d.setBounds(0, 0, c.getWidth(), c.getHeight());
      d.draw(c);
      remoteViews.setImageViewBitmap(resId, b);
    }
  }

  private PendingIntent getPendingIntent(String action) {
    return PendingIntent.getService(context, 0, new Intent(action), 0);
  }

  private RemoteViews bigRemoteView() {
    if (bigRemoteView == null) {
      bigRemoteView = new RemoteViews(context.getPackageName(), R.layout.remote_view_music_player);
      setUpRemoteView(bigRemoteView);
    }
    return bigRemoteView;
  }

  public void show() {
    Observable.just(musicPlayer.getPlayingSong())
        .subscribeOn(Schedulers.io())
        .flatMap(new Function<Music, ObservableSource<Pair<Music, Bitmap>>>() {
          @Override public ObservableSource<Pair<Music, Bitmap>> apply(Music music)
              throws Exception {
            String albumArt = albumTable.albumById(music.media().albumId()).albumArt();
            if (albumArt == null) {
              return Observable.just(new Pair<>(music, Glide.with(context)
                  .load(R.mipmap.ic_launcher)
                  .asBitmap()
                  .skipMemoryCache(false)
                  .into(205, 205)
                  .get()));
            } else {
              return Observable.just(new Pair<>(music, Glide.with(context)
                  .load(albumArt)
                  .asBitmap()
                  .skipMemoryCache(false)
                  .error(R.mipmap.ic_launcher)
                  .into(205, 205)
                  .get()));
            }
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<Pair<Music, Bitmap>>() {
          @Override public void onNext(Pair<Music, Bitmap> pair) {
            Music music = pair.first;
            Bitmap album = pair.second;
            updateRemoteViews(smallRemoteView(), music, album);
            updateRemoteViews(bigRemoteView(), music, album);
            updateMediaSessionMetaData(music,
                new BitmapImage(album, context.getResources()).blurred(68).saturated().bitmap());
            // The PendingIntent to launch our activity if the user selects this notification
            PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new NotificationCompat.Builder(context).setSmallIcon(
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

          @Override public void onError(Throwable e) {
          }

          @Override public void onComplete() {
          }
        });
  }

  private void updateRemoteViews(final RemoteViews remoteView, Music music, Bitmap album) {
    remoteView.setTextViewText(R.id.nameTextView, music.media().title());
    remoteView.setTextViewText(R.id.text_view_artist, music.media().artist());
    setVectorDrawable(remoteView, R.id.image_view_play_toggle,
        musicPlayer.isPlaying() ? R.drawable.ic_remote_view_pause : R.drawable.ic_remote_view_play);
    remoteView.setImageViewBitmap(R.id.albumImageView, album);
  }

  private void updateMediaSessionMetaData(Music music, Bitmap bitmap) {
    mediaSessionCompat.setMetadata(
        new MediaMetadataCompat.Builder().putString(MediaMetadataCompat.METADATA_KEY_ARTIST,
            music.media().artist())
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, music.media().album())
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.media().title())
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 10000)
            .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
            .build());
  }
}