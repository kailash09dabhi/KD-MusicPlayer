package com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed;

import android.support.annotation.NonNull;
import com.kingbull.musicplayer.ui.base.Presenter;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class LastPlayedPresenter extends Presenter<LastPlayed.View>
    implements LastPlayed.Presenter {

  LastPlayed.Model model = new LastPlayedModel();

  @Override public void takeView(@NonNull LastPlayed.View view) {
    super.takeView(view);
    view().showLastPlayedMusic(model.lastPlayedMusicList());
  }
}
