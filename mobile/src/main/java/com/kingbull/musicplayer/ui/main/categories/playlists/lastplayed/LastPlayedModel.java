package com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.MusicTable;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */
public final class LastPlayedModel implements LastPlayed.Model {
  @Inject MusicTable musicTable;

  public LastPlayedModel() {
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public List<Music> lastPlayedMusicList() {
    return musicTable.lastPlayedSongs();
  }
}
