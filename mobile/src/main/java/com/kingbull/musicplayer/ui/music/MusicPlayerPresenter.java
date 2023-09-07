package com.kingbull.musicplayer.ui.music;

import androidx.annotation.NonNull;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.event.MusicEvent;
import com.kingbull.musicplayer.player.MusicMode;
import com.kingbull.musicplayer.player.MusicPlayerEvent;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;
import javax.inject.Inject;

public final class MusicPlayerPresenter extends Presenter<MusicPlayer.View>
    implements MusicPlayer.Presenter {
  private static final long UPDATE_PROGRESS_INTERVAL = 1000;
  @Inject Player player;
  @Inject SettingPreferences settingPreferences;
  private Music currentPlayingSong;
  private long currentPlayingSongDuration;

  @Override public void takeView(@NonNull MusicPlayer.View view) {
    super.takeView(view);
    if (!player.nowPlayingMusicList().isEmpty()) {
      currentPlayingSong = player.getPlayingSong();
      currentPlayingSongDuration = currentPlayingSong.media().duration();
      view().displayNewSongInfo(currentPlayingSong);
    } else {
      FirebaseCrashlytics.getInstance().recordException(new RuntimeException("How we get into this state? "
          + "We are in MusicPlayer screen and NowPlayingList is empty! Just go back to Main screen!"));
      view().close();
    }
  }

  @Override public void onFavoriteToggleClick() {
    currentPlayingSong.mediaStat().toggleFavourite();
    view().updateFavoriteToggle(currentPlayingSong.mediaStat().isFavorite());
  }

  @Override public void onPlayNextClick() {
    player.playNext();
  }

  @Override public void onPlayPreviousClick() {
    player.playPrevious();
  }

  @Override public void onPlayModeToggleClick() {
    MusicMode current = settingPreferences.musicMode();
    MusicMode newMode = MusicMode.switchNextMode(current);
    settingPreferences.saveMusicMode(newMode);
    view().updatePlayMode(newMode);
  }

  @Override public void onPlayToggleClick() {
    if (player.isPlaying()) {
      player.pause();
    } else {
      player.play();
    }
  }

  @Override public void onSeekbarProgress() {
    if (player.isPlaying()) {
      int progress =
          (int) (100 * ((float) player.getProgress() / (float) currentPlayingSongDuration));
      view().updateProgressDurationText(player.getProgress());
      if (progress >= 0 && progress <= 100) {
        view().updateSeekBar(progress);
        view().updateSeekBarAfter(UPDATE_PROGRESS_INTERVAL);
      }
    }
  }

  @Override public void onStopTrackingTouch(int progress) {
    player.seekTo((int) (currentPlayingSongDuration * ((float) progress / 100)));
    if (player.isPlaying()) {
      view().stopSeekbarProgress();
    }
  }

  @Override public void onProgressChanged(int duration) {
    view().updateProgressDurationText(duration);
    view().updateSeekBar((int) ((duration / (float) currentPlayingSongDuration) * 100));
  }

  @Override public void onEqualizerClick() {
    view().gotoShowEqualizerScreen();
  }

  @Override public void onMusicEvent(MusicEvent musicEvent) {
    int eventType = musicEvent.musicPlayerEvent();
    if (eventType == MusicPlayerEvent.PLAY) {
      view().displayPauseButton();
      view().startAlbumImageRotationAnimation();
      view().startProgressAnimation();
    } else if (eventType == MusicPlayerEvent.PAUSE) {
      view().displayPlayButton();
      view().stopAlbumImageRotationAnimation();
      view().stopProgressAnimation();
    } else if (eventType == MusicPlayerEvent.COMPLETED) {
      view().stopProgressAnimation();
      view().updateSeekBar(100);
      view().updateProgressDurationText((int) musicEvent.music().media().duration());
      view().displayPlayButton();
      view().stopAlbumImageRotationAnimation();
    }
    if (currentPlayingSong != null && !currentPlayingSong.equals(musicEvent.music())) {
      currentPlayingSong = musicEvent.music();
      currentPlayingSongDuration = currentPlayingSong.media().duration();
      view().displayNewSongInfo(currentPlayingSong);
    }
  }

  @Override public void onNowPlayingClick() {
    if (!player.nowPlayingMusicList().isEmpty()) {
      view().gotoNowPlayingListScreen();
    } else {
      FirebaseCrashlytics.getInstance().recordException(new RuntimeException("How we get into this state? "
          + "User clicked NowPlayingList but it is empty! Just go back to Main screen!"));
      view().close();
    }
  }
}
