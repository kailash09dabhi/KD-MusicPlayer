package com.kingbull.musicplayer.ui.main.categories.playlists.members;

import androidx.annotation.NonNull;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class MembersPresenter extends Presenter<Members.View>
    implements Members.Presenter {

  @Inject Player musicPlayer;

  @Override public void takeView(@NonNull Members.View view) {
    super.takeView(view);
  }
}
