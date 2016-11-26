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

  class Smart extends ArrayList<Music> implements NowPlayingList {
    private static final int NO_POSITION = -1;
    private int currentRunningMusicIndex = NO_POSITION;

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
      if (currentRunningMusicIndex >= size() - 1) {
        currentRunningMusicIndex = 0;
      } else {
        currentRunningMusicIndex++;
      }
      return get(currentRunningMusicIndex);
    }

    @Override public Music previous() {
      if (currentRunningMusicIndex <= 0) {
        currentRunningMusicIndex = size() - 1;
      } else {
        currentRunningMusicIndex--;
      }
      return get(currentRunningMusicIndex);
    }

    @Override public boolean addAll(Collection<? extends Music> c) {
      boolean isChanged = super.addAll(c);
      currentRunningMusicIndex = c.size() > 0 ? 0 : NO_POSITION;
      return isChanged;
    }
  }
}
