package com.kingbull.musicplayer.ui.music;

import android.support.annotation.Nullable;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.player.PlayMode;
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

    void onSongUpdated(@Nullable Music song);

    void updatePlayMode(PlayMode playMode);

    void updateFavoriteToggle(boolean favorite);

    void updateProgressDurationText(int duration);

    void updateSeekBar(int progress);

    void updateSeekBarAfter(long updateProgressInterval);

    void onPlayStatusChanged(boolean isPlaying);

    void stopSeekbarProgress();

  }

  interface Presenter extends Mvp.Presenter<MusicPlayer.View> {

    void onTakePlayBack(Player player);

    void onTakeSong(Music song);

    void onFavoriteToggleClick();

    void onPlayLastClick();

    void onPlayNextClick();

    void onPlayModeToggleClick();

    void onPlayToggleClick();

    void onSeekbarProgress();

    void seekTo(int durationTime);

    void onStopTrackingTouch(int progress);

    void onProgressChanged(int i);

  }
}
