package com.kingbull.musicplayer.ui.music;

import android.support.annotation.NonNull;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.SettingPreferences;
import com.kingbull.musicplayer.player.MusicMode;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;
import javax.inject.Inject;

public final class MusicPlayerPresenter extends Presenter<MusicPlayer.View>
    implements MusicPlayer.Presenter {
  private static final long UPDATE_PROGRESS_INTERVAL = 1000;
  @Inject Player player;

  @Override public void takeView(@NonNull MusicPlayer.View view) {
    super.takeView(view);
    view().onSongUpdated(player.getPlayingSong());
  }

  @Override public void onFavoriteToggleClick() {
    if (player == null) return;
    Music currentSong = player.getPlayingSong();
    if (currentSong != null) {
    }
  }

  @Override public void onPlayNextClick() {
    if (player == null) return;
    player.playNext();
  }

  @Override public void onPlayPreviousClick() {
    player.playPrevious();
  }

  @Override public void onPlayModeToggleClick() {
    if (player == null) return;
    SettingPreferences prefs = new SettingPreferences();
    MusicMode current = prefs.musicMode();
    MusicMode newMode = MusicMode.switchNextMode(current);
    prefs.saveMusicMode(newMode);
    view().updatePlayMode(newMode);
  }

  @Override public void onPlayToggleClick() {
    if (player == null) return;
    if (player.isPlaying()) {
      view().pause();
      player.pause();
    } else {
      player.play();
      view().play();
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

  @Override public void onEqualizerClick() {
    view().showEqualizerScreen(player.audioSessionId());
  }

  public long getCurrentSongDuration() {
    Music currentSong = player.getPlayingSong();
    long duration = 0;
    if (currentSong != null) {
      duration = currentSong.duration();
    }
    return duration;
  }
}
