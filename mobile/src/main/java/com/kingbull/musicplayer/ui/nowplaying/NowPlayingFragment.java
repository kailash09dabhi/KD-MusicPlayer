/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.nowplaying;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import java.util.List;

public final class NowPlayingFragment extends BaseFragment<NowPlaying.Presenter>
    implements NowPlaying.View {

  public static NowPlayingFragment instance(int audioSessionId) {
    NowPlayingFragment equalizerFragment = new NowPlayingFragment();
    Bundle args = new Bundle();
    args.putInt("audio_session_id", audioSessionId);
    equalizerFragment.setArguments(args);
    return equalizerFragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_playlist, null);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override protected void onPresenterPrepared(NowPlaying.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override protected PresenterFactory<NowPlaying.Presenter> presenterFactory() {
    return new PresenterFactory.NowPlaying();
  }

  @Override public void showNowPlayingList(List<Music> musicList) {
  }
}
