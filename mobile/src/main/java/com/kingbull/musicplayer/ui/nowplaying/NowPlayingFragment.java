
package com.kingbull.musicplayer.ui.nowplaying;

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
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.StatusBarColor;
import com.kingbull.musicplayer.ui.base.ads.AdmobBannerLoaded;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 23rd Nov, 2016 2:57 PM
 */
public final class NowPlayingFragment extends BaseFragment<NowPlaying.Presenter>
    implements NowPlaying.View {
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.shuffleButton) ImageView shuffleButton;

  public static NowPlayingFragment newInstance(int statusBarColor) {
    NowPlayingFragment nowPlayingFragment = new NowPlayingFragment();
    Bundle bundle = new Bundle();
    bundle.putInt("statusBarColor", statusBarColor);
    nowPlayingFragment.setArguments(bundle);
    return nowPlayingFragment;
  }

  @OnClick(R.id.shuffleButton) void onShuffleClick() {
    presenter.onShuffleClick();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_now_playing_list, container, false);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    setupView(view);
    return view;
  }

  private void setupView(View v) {
    new StatusBarColor(flatTheme.statusBar()).applyOn(getActivity().getWindow());
    com.kingbull.musicplayer.ui.base.Color color =
        new com.kingbull.musicplayer.ui.base.Color(flatTheme.statusBar().intValue());
    v.setBackground(color.light().toDrawable());
    shuffleButton.setImageDrawable(
        new IconDrawable(R.drawable.ic_shuffle_48dp, flatTheme.statusBar().intValue()));
    titleView.setBackgroundColor(flatTheme.header().intValue());
    titleView.setText("Now Playing".toUpperCase());
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    new AdmobBannerLoaded((ViewGroup) v);
  }

  @Override public void onDestroyView() {
    new StatusBarColor(getArguments().getInt("statusBarColor", 0)).applyOn(
        getActivity().getWindow());
    super.onDestroyView();
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
