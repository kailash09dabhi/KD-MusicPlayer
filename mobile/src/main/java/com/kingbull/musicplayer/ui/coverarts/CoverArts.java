package com.kingbull.musicplayer.ui.coverarts;

import com.kingbull.musicplayer.ui.base.Mvp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 12/25/2016.
 */

public interface CoverArts extends Mvp {
  interface View extends Mvp.View {
    void showCoverArts(List<String> imageUrls);

    void dismissProgress();

    void showProgress();

    void showNoResultFoundMessage();
  }

  interface Model extends Mvp.Model {
  }

  interface Presenter extends Mvp.Presenter<CoverArts.View> {
    void onAlbumSearch(String album);

    void onArtistSearch(String artist);

    void onCoverSelection();
  }
}
