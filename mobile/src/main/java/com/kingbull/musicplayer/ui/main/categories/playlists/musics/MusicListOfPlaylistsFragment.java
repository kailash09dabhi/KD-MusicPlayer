/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.categories.playlists.musics;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import com.kingbull.musicplayer.domain.storage.PlayList;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.songlist.SongsAdapter;
import java.util.List;

public final class MusicListOfPlaylistsFragment extends BaseFragment<MusicListOfPlaylist.Presenter>
    implements MusicListOfPlaylist.View {
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  PlayList playList;

  public static MusicListOfPlaylistsFragment newInstance(PlayList playList) {
    MusicListOfPlaylistsFragment fragment = new MusicListOfPlaylistsFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelable("playlist", (Parcelable) playList);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_playlist, null);
    ButterKnife.bind(this, view);
    playList = getArguments().getParcelable("playlist");
    return view;
  }

  @Override protected void onPresenterPrepared(MusicListOfPlaylist.Presenter presenter) {
    presenter.takeView(this);
    setupView(getView());
  }

  @NonNull @Override protected PresenterFactory<MusicListOfPlaylist.Presenter> presenterFactory() {
    return new PresenterFactory.MusicListOfPlaylist();
  }

  private void setupView(View v) {
    titleView.setText(playList.name().toUpperCase());
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    showMusicListOfPlaylist(playList.musicList());
  }

  @Override public void showMusicListOfPlaylist(List<Music> songs) {
    recyclerView.setAdapter(new SongsAdapter(songs));
  }
}
