/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.nowplaying;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import java.util.List;

public final class NowPlayingFragment extends BaseFragment<NowPlaying.Presenter>
    implements NowPlaying.View {
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.shuffleButton) ImageView shuffleButton;

  @OnClick(R.id.shuffleButton) void onShuffleClick() {
    presenter.onShuffleClick();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_now_playing_list, null);
    ButterKnife.bind(this, view);
    setupView(view);
    return view;
  }

  private void setupView(View v) {
    com.kingbull.musicplayer.ui.base.Color color =
        new com.kingbull.musicplayer.ui.base.Color(colorTheme.screen().intValue());
    v.setBackground(color.light().toDrawable());
    shuffleButton.setImageDrawable(new IconDrawable(R.drawable.ic_shuffle_48dp, Color.WHITE,
        colorTheme.statusBar().intValue()));
    titleView.setText("Now Playing".toUpperCase());
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
  }

  @Override protected PresenterFactory<NowPlaying.Presenter> presenterFactory() {
    return new PresenterFactory.NowPlaying();
  }

  @Override protected void onPresenterPrepared(NowPlaying.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override public void showNowPlayingList(List<Music> musicList, int position) {
    NowPlayingAdapter adapter = new NowPlayingAdapter(musicList);
    recyclerView.setAdapter(adapter);
    recyclerView.scrollToPosition(position);
    // Setup onItemTouchHandler
    ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(adapter, true, true, true);
    ItemTouchHelper helper = new ItemTouchHelper(callback);
    helper.attachToRecyclerView(recyclerView);
  }
}
