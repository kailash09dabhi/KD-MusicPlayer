package com.kingbull.musicplayer.ui.main.categories.playlists;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */

public final class PlayListsModel implements PlayLists.Model {

  @Override public List<PlayList> listOfPlayList() {
    PlayList playList1 = new PlayList("Recently Added");
    PlayList playList2 = new PlayList("Last Played");
    PlayList playList3 = new PlayList("Most Played");
    PlayList playList4 = new PlayList("FM Recordings");
    return Arrays.asList(playList1, playList2, playList3, playList4);
  }
}
