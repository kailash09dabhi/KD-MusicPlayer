package com.kingbull.musicplayer.player;

import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.equalizer.reverb.Reverb;
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

  boolean playPrevious();

  boolean playNext();

  boolean pause();

  boolean isPlaying();

  void setVolume(float volume);

  int getProgress();

  Music getPlayingSong();

  Equalizer equalizer();

  boolean seekTo(int progress);

  void useEffect(Reverb reverb);

  BassBoost bassBoost();

  Virtualizer virtualizer();

  void releasePlayer();

  void addToNowPlaylist(List<Music> songs);

  NowPlayingList nowPlayingMusicList();
}