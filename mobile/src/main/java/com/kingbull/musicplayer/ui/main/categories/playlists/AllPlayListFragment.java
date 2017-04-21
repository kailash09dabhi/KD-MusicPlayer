/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.categories.playlists;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.PlaylistCreatedEvent;
import com.kingbull.musicplayer.event.PlaylistRenameEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;

public final class AllPlayListFragment extends BaseFragment<AllPlaylist.Presenter>
    implements AllPlaylist.View {
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.headerLayout) LinearLayout headerLayout;
  @BindView(R.id.header) TextView headerView;
  @BindView(R.id.description) TextView descriptionView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_all_playlist, container, false);
    ButterKnife.bind(this, view);
    setupView();
    return view;
  }

  private void setupView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setBackgroundColor(smartColorTheme.screen().intValue());
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof PlaylistCreatedEvent) {
              PlaylistCreatedEvent playlistCreatedEvent = (PlaylistCreatedEvent) o;
              presenter.onPlaylistCreated(playlistCreatedEvent.playList());
            } else if (o instanceof PlaylistRenameEvent) {
              presenter.onPlaylistRename((PlaylistRenameEvent) o);
            } else if (o instanceof PaletteEvent || o instanceof ThemeEvent) {
              recyclerView.setBackgroundColor(smartColorTheme.screen().intValue());
              headerLayout.setBackgroundColor(smartColorTheme.header().intValue());
              headerView.setTextColor(smartColorTheme.titleText().intValue());
              descriptionView.setTextColor(smartColorTheme.bodyText().intValue());
            }
          }
        });
  }

  @Override protected PresenterFactory<AllPlaylist.Presenter> presenterFactory() {
    return new PresenterFactory.AllPlaylist();
  }

  @Override protected void onPresenterPrepared(AllPlaylist.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override public void showAllPlaylist(List<PlayList> playLists) {
    recyclerView.setAdapter(new AllPlaylistAdapter(playLists, (AppCompatActivity) getActivity()));
  }

  @Override public void refreshListOfPlaylist() {
    recyclerView.getAdapter().notifyDataSetChanged();
  }

  @Override public void showMessage(String message) {
    new Snackbar(recyclerView).show(message);
  }
}
