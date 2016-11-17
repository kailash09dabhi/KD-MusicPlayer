package com.kingbull.musicplayer.ui.main.categories.playlists.mostplayed;

import android.support.annotation.NonNull;
import com.kingbull.musicplayer.ui.base.Presenter;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class MostPlayedPresenter extends Presenter<MostPlayed.View>
    implements MostPlayed.Presenter {

  MostPlayed.Model model = new MostPlayedModel();

  @Override public void takeView(@NonNull MostPlayed.View view) {
    super.takeView(view);
    view().showMostPlayedMusic(model.mostPlayedMusicList());
  }
}
