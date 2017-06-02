package com.kingbull.musicplayer.ui.music;

import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.event.MusicEvent;
import com.kingbull.musicplayer.player.MusicMode;
import com.kingbull.musicplayer.ui.base.Mvp;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/12/16
 * Time: 8:27 AM
 * Desc: MusicPlayer
 */
public interface MusicPlayer {

  interface View extends Mvp.View {

    void updatePlayMode(MusicMode musicMode);

    void updateFavoriteToggle(boolean favorite);

    void updateProgressDurationText(int duration);

    void updateSeekBar(int progress);

    void updateSeekBarAfter(long updateProgressInterval);

    void stopSeekbarProgress();

    void gotoShowEqualizerScreen();

    void pause();

    void play();

    void displayPauseButton();

    void displayPlayButton();

    void startAlbumImageRotationAnimation();

    void startProgressAnimation();

    void stopAlbumImageRotationAnimation();

    void stopProgressAnimation();

    void displayNewSongInfo(Music music);

    void gotoNowPlayingListScreen();

    void close();
  }

  interface Presenter extends Mvp.Presenter<MusicPlayer.View> {

    void onFavoriteToggleClick();

    void onPlayNextClick();

    void onPlayPreviousClick();

    void onPlayModeToggleClick();

    void onPlayToggleClick();

    void onSeekbarProgress();

    void onStopTrackingTouch(int progress);

    void onProgressChanged(int i);

    void onEqualizerClick();

    void onMusicEvent(MusicEvent musicEvent);

    void onNowPlayingClick();
  }
}
