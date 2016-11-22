package com.kingbull.musicplayer.player;

import android.support.annotation.Nullable;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.PlayList;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/5/16
 * Time: 6:02 PM
 * Desc: IPlayer
 */
public interface Player {

  void setPlayList(PlayList list);

  boolean play();

  boolean play(PlayList list);

  boolean play(PlayList list, int startIndex);

  boolean play(Music song);

  boolean playLast();

  boolean playNext();

  boolean pause();

  boolean isPlaying();

  int getProgress();

  Music getPlayingSong();

  boolean seekTo(int progress);

  void setPlayMode(PlayMode playMode);

  void registerCallback(Callback callback);

  void unregisterCallback(Callback callback);

  void removeCallbacks();

  void releasePlayer();

  interface Callback {

    void onSwitchLast(@Nullable Music last);

    void onSwitchNext(@Nullable Music next);

    void onComplete(@Nullable Music next);

    void onPlayStatusChanged(boolean isPlaying);
  }
}
