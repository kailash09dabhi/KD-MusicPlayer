package com.kingbull.musicplayer.ui.music;

import android.support.annotation.Nullable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.PreferenceManager;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.player.IPlayback;
import com.kingbull.musicplayer.player.PlayMode;
import com.kingbull.musicplayer.ui.base.Presenter;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/12/16
 * Time: 8:30 AM
 * Desc: MusicPlayerPresenter
 */
public final class MusicPlayerPresenter extends Presenter<MusicPlayer.View>
    implements MusicPlayer.Presenter, IPlayback.Callback {
  private static final long UPDATE_PROGRESS_INTERVAL = 1000;
  private IPlayback player;
  private Song song;

  @Override public void onTakePlayBack(IPlayback iPlayback) {
    if (iPlayback == null) {
      player.unregisterCallback(this);
      player = null;
    } else {
      player = iPlayback;
      player.registerCallback(this);
      view().onSongUpdated(song);
    }
  }

  @Override public void onTakeSong(Song song) {
    this.song = song;
  }

  @Override public void onFavoriteToggleClick() {
    if (player == null) return;
    Song currentSong = player.getPlayingSong();
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

  public int getCurrentSongDuration() {
    Song currentSong = player.getPlayingSong();
    int duration = 0;
    if (currentSong != null) {
      duration = currentSong.getDuration();
    }
    return duration;
  }

  @Override public void onSwitchLast(@Nullable Song last) {
    view().onSongUpdated(last);
  }

  @Override public void onSwitchNext(@Nullable Song next) {
    view().onSongUpdated(next);
  }

  @Override public void onComplete(@Nullable Song next) {
    view().onSongUpdated(next);
  }

  @Override public void onPlayStatusChanged(boolean isPlaying) {
    view().onPlayStatusChanged(isPlaying);
  }
}
