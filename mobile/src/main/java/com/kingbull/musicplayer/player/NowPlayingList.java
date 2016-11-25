package com.kingbull.musicplayer.player;

import com.kingbull.musicplayer.domain.Music;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/25/2016.
 */

public interface NowPlayingList extends List<Music> {

  Music currentMusic();

  void jumpTo(Music music);

  Music next();

  Music previous();

  void useMusicMode(PlayMode playmode);

  class Smart extends ArrayList<Music> implements NowPlayingList {
    private static final int NO_POSITION = -1;
    private int currentRunningMusicIndex = NO_POSITION;
    private PlayMode playMode;

    @Override public Music currentMusic() {
      if (isEmpty() && currentRunningMusicIndex == NO_POSITION) {
        throw new IllegalStateException("current running music index is -1");
      }
      return get(currentRunningMusicIndex);
    }

    @Override public void jumpTo(Music music) {
      currentRunningMusicIndex = indexOf(music);
    }

    @Override public Music next() {
      return get(++currentRunningMusicIndex);
    }

    @Override public Music previous() {
      return get(--currentRunningMusicIndex);
    }

    @Override public void useMusicMode(PlayMode playmode) {
      this.playMode = playmode;
    }

    @Override public boolean addAll(Collection<? extends Music> c) {
      boolean isChanged = super.addAll(c);
      currentRunningMusicIndex = c.size() > 0 ? 0 : NO_POSITION;
      return isChanged;
    }
  }
}
