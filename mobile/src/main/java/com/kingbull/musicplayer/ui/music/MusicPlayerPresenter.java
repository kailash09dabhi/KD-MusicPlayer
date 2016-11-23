package com.kingbull.musicplayer.ui.music;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.PreferenceManager;
import com.kingbull.musicplayer.player.PlayMode;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/12/16
 * Time: 8:30 AM
 * Desc: MusicPlayerPresenter
 */
public final class MusicPlayerPresenter extends Presenter<MusicPlayer.View>
    implements MusicPlayer.Presenter, Player.Callback {
  private static final long UPDATE_PROGRESS_INTERVAL = 1000;
  private Player player;
  private Music song;

  @Override public void takeView(@NonNull MusicPlayer.View view) {
    super.takeView(view);
    if (view == null) player.unregisterCallback(this);
  }

  @Override public void onTakePlayBack(Player player) {
    if (player == null) {
      this.player.unregisterCallback(this);
      this.player = null;
    } else {
      this.player = player;
      this.player.registerCallback(this);
      view().onSongUpdated(song);
    }
  }

  @Override public void onTakeSong(Music song) {
    this.song = song;
  }

  @Override public void onFavoriteToggleClick() {
    if (player == null) return;
    Music currentSong = player.getPlayingSong();
    if (currentSong != null) {
    }
  }

  @Override public void onPlayLastClick() {
    if (player == null) return;
    player.playLast();
  }

  @Override public void onPlayNextClick() {
    if (player == null) return;
    player.playNext();
  }

  @Override public void onPlayModeToggleClick() {
    if (player == null) return;
    PlayMode current = PreferenceManager.lastPlayMode(MusicPlayerApp.instance());
    PlayMode newMode = PlayMode.switchNextMode(current);
    PreferenceManager.setPlayMode(MusicPlayerApp.instance(), newMode);
    player.setPlayMode(newMode);
    view().updatePlayMode(newMode);
  }

  @Override public void onPlayToggleClick() {
    if (player == null) return;
    if (player.isPlaying()) {
      player.pause();
    } else {
      if (player.getPlayingSong() != song) {
        player.play(song);
      } else {
        player.play();
      }
    }
  }

  @Override public void onSeekbarProgress() {
    if (player.isPlaying()) {
      int progress =
          (int) (100 * ((float) player.getProgress() / (float) getCurrentSongDuration()));
      view().updateProgressDurationText(player.getProgress());
      if (progress >= 0 && progress <= 100) {
        view().updateSeekBar(progress);
        view().updateSeekBarAfter(UPDATE_PROGRESS_INTERVAL);
      }
    }
  }

  @Override public void seekTo(int duration) {
    player.seekTo(duration);
  }

  @Override public void onStopTrackingTouch(int progress) {
    seekTo((int) (getCurrentSongDuration() * ((float) progress / 100)));
    if (player.isPlaying()) {
      view().stopSeekbarProgress();
    }
  }

  @Override public void onProgressChanged(int duration) {
    view().updateProgressDurationText(duration);
  }

  public long getCurrentSongDuration() {
    Music currentSong = player.getPlayingSong();
    long duration = 0;
    if (currentSong != null) {
      duration = currentSong.duration();
    }
    return duration;
  }

  @Override public void onSwitchLast(@Nullable Music last) {
    view().onSongUpdated(last);
  }

  @Override public void onSwitchNext(@Nullable Music next) {
    view().onSongUpdated(next);
  }

  @Override public void onComplete(@Nullable Music next) {
    view().onSongUpdated(next);
  }

  @Override public void onPlayStatusChanged(boolean isPlaying) {
    view().onPlayStatusChanged(isPlaying);
  }
}
