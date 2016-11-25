package com.kingbull.musicplayer.player;

import android.support.annotation.Nullable;
import com.kingbull.musicplayer.domain.Music;
import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/5/16
 * Time: 6:02 PM
 * Desc: IPlayer
 */
public interface Player {

  boolean play();
  boolean play(Music music);

  boolean playLast();

  boolean playNext();

  boolean pause();

  boolean isPlaying();

  int getProgress();

  int audioSessionId();

  Music getPlayingSong();

  boolean seekTo(int progress);

  void setPlayMode(PlayMode playMode);

  void registerCallback(Callback callback);

  void unregisterCallback(Callback callback);

  void removeCallbacks();

  void releasePlayer();

  void addToNowPlaylist(List<Music> songs);

  NowPlayingList nowPlayingMusicList();

  interface Callback {

    void onSwitchLast(@Nullable Music last);

    void onSwitchNext(@Nullable Music next);

    void onComplete(@Nullable Music next);

    void onPlayStatusChanged(boolean isPlaying);
  }
}
