package com.kingbull.musicplayer.ui.main.categories.playlists.members;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.event.MovedToPlaylistEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.ads.AdmobNativeBannerLoaded;
import com.kingbull.musicplayer.ui.base.animators.Alpha;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;

public final class MembersFragment extends BaseFragment<Members.Presenter> implements Members.View {
  private final Alpha.Animation alphaAnimation = new Alpha.Animation();
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.multipleDeleteView) ImageView multipleDeleteView;
  private final MembersRecyclerViewAdapter.OnSelectionListener onSelectionListener =
      new MembersRecyclerViewAdapter.OnSelectionListener() {
        @Override public void onClearSelection() {
          alphaAnimation.fadeOut(multipleDeleteView);
        }

        @Override public void onMultiSelection(int selectionCount) {
          alphaAnimation.fadeIn(multipleDeleteView);
        }
      };
  private PlayList playList;
  private List<Music> musicList;

  public static MembersFragment newInstance(PlayList playList) {
    MembersFragment fragment = new MembersFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelable("playlist", (Parcelable) playList);
    fragment.setArguments(bundle);
    return fragment;
  }

  @OnClick(R.id.multipleDeleteView) void onMultipleDeleteClick() {
    ((MembersRecyclerViewAdapter) recyclerView.getAdapter()).deleteSelectedMusicFromPlaylist();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_playlist, container, false);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    new AdmobNativeBannerLoaded(view);
    playList = getArguments().getParcelable("playlist");
    return view;
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .ofType(MovedToPlaylistEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<MovedToPlaylistEvent>() {
          @Override public void accept(MovedToPlaylistEvent movedToPlaylistEvent) throws Exception {
            new Snackbar(recyclerView).show(
                "Song has benn moved to " + movedToPlaylistEvent.destinationPlaylistName());
            musicList.remove(movedToPlaylistEvent.position());
            recyclerView.getAdapter().notifyItemRemoved(movedToPlaylistEvent.position());
          }
        });
  }

  @NonNull @Override protected PresenterFactory<Members.Presenter> presenterFactory() {
    return new PresenterFactory.MusicListOfPlaylist();
  }

  @Override protected void onPresenterPrepared(Members.Presenter presenter) {
    presenter.takeView(this);
    setupView();
  }

  private void setupView() {
    initializeWithThemeColors();
    titleView.setText(playList.name().toUpperCase());
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    musicList = playList.musicList();
    multipleDeleteView.setImageDrawable(
        new IconDrawable(R.drawable.ic_delete_48dp, flatTheme.header().intValue()));
    showPlaylistMembers(musicList);
  }

  private void initializeWithThemeColors() {
    titleView.setBackgroundColor(flatTheme.header().intValue());
    getView().setBackgroundColor(flatTheme.screen().intValue());
  }

  @Override public void showPlaylistMembers(List<Music> songs) {
    MembersRecyclerViewAdapter adapter =
        new MembersRecyclerViewAdapter(playList, songs, (AppCompatActivity) getActivity());
    adapter.addOnSelectionListener(onSelectionListener);
    recyclerView.setAdapter(adapter);
  }
}
