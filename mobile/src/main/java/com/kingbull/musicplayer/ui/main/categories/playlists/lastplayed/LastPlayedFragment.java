/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.songlist.SongsAdapter;
import java.util.List;

public final class LastPlayedFragment extends BaseFragment<LastPlayed.Presenter>
    implements LastPlayed.View {
  private RecyclerView recyclerView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_genres, null);
    setupView(view);
    return view;
  }

  private void setupView(View v) {
    recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    v.setBackgroundResource(R.drawable.artistbg);
  }

  @Override public void showLastPlayedMusic(List<Music> lastPlayedMusic) {
    recyclerView.setAdapter(new SongsAdapter(lastPlayedMusic));
  }

  @Override protected void onPresenterPrepared(LastPlayed.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override protected PresenterFactory<LastPlayed.Presenter> presenterFactory() {
    return new PresenterFactory.LastPlayed();
  }
}
