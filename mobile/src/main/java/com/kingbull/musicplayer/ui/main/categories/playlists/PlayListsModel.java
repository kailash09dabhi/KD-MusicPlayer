package com.kingbull.musicplayer.ui.main.categories.playlists;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.LastPlayedPlayList;
import com.kingbull.musicplayer.domain.storage.MostPlayedPlayList;
import com.kingbull.musicplayer.domain.storage.PlayList;
import com.kingbull.musicplayer.domain.storage.PlayListTable;
import com.kingbull.musicplayer.domain.storage.RecentlyAddedPlayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */

public final class PlayListsModel implements PlayLists.Model {
  @Inject PlayListTable playListTable;

  PlayListsModel() {
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public List<PlayList> listOfPlayList() {
    PlayList playList1 = new RecentlyAddedPlayList();
    PlayList playList2 = new LastPlayedPlayList();
    PlayList playList3 = new MostPlayedPlayList();
    List<com.kingbull.musicplayer.domain.storage.PlayList> playLists = playListTable.playlists();
    playLists.add(0, playList1);
    playLists.add(1, playList2);
    playLists.add(2, playList3);
    return playLists;
  }
}
