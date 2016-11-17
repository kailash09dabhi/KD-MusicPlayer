package com.kingbull.musicplayer.ui.main.categories.playlists.mostplayed;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.MusicTable;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */
public final class MostPlayedModel implements MostPlayed.Model {
  @Inject MusicTable musicTable;

  public MostPlayedModel() {
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public List<Music> mostPlayedMusicList() {
    return musicTable.mostPlayedSongs();
  }
}
