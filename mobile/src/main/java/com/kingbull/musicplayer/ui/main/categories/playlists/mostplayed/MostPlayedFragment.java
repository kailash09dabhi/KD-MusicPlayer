/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.categories.playlists.mostplayed;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.songlist.SongsAdapter;
import java.util.List;

public final class MostPlayedFragment extends BaseFragment<MostPlayed.Presenter>
    implements MostPlayed.View {
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_playlist, null);
    ButterKnife.bind(this, view);
    setupView(view);
    return view;
  }

  private void setupView(View v) {
    titleView.setText("Most Played".toUpperCase());
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
  }

  @Override public void showMostPlayedMusic(List<Music> mostPlayedMusic) {
    recyclerView.setAdapter(new SongsAdapter(mostPlayedMusic));
  }

  @Override protected void onPresenterPrepared(MostPlayed.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override protected PresenterFactory<MostPlayed.Presenter> presenterFactory() {
    return new PresenterFactory.MostPlayed();
  }

  public void refresh() {
    if (presenter != null) presenter.takeView(this);
  }
}
