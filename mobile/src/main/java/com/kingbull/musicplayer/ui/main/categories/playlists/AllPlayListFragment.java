package com.kingbull.musicplayer.ui.main.categories.playlists;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.PlaylistCreatedEvent;
import com.kingbull.musicplayer.event.PlaylistRenameEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.event.TransparencyChangedEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.ads.AdmobBannerLoaded;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import com.kingbull.musicplayer.ui.main.categories.playlists.members.MembersFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 8th Nov, 2016 9:09 PM
 */
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
    MusicPlayerApp.instance().component().inject(this);
    new AdmobBannerLoaded((ViewGroup) view);
    setupView();
    return view;
  }

  private void setupView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setBackgroundColor(smartTheme.screen().intValue());
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> {
          if (presenter != null && presenter.hasView()) {
            if (o instanceof PlaylistCreatedEvent) {
              PlaylistCreatedEvent playlistCreatedEvent = (PlaylistCreatedEvent) o;
              presenter.onPlaylistCreated(playlistCreatedEvent.playList());
            } else if (o instanceof PlaylistRenameEvent) {
              presenter.onPlaylistRename((PlaylistRenameEvent) o);
            } else if (o instanceof PaletteEvent || o instanceof ThemeEvent ||
                o instanceof TransparencyChangedEvent) {
              recyclerView.setBackgroundColor(smartTheme.screen().intValue());
              headerLayout.setBackgroundColor(smartTheme.header().intValue());
              headerView.setTextColor(smartTheme.titleText().intValue());
              descriptionView.setTextColor(smartTheme.bodyText().intValue());
            }
          } else {
            FirebaseCrashlytics.getInstance().recordException(
                new NullPointerException(
                    String.format(
                        "class: %s presenter- %s hasView- %b",
                        AllPlayListFragment.class.getSimpleName(),
                        presenter, presenter != null && presenter.hasView()
                    )
                )
            );
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
    recyclerView.setAdapter(
        new AllPlaylistAdapter(playLists, (AppCompatActivity) getActivity(), presenter));
  }

  @Override public void refreshListOfPlaylist() {
    recyclerView.getAdapter().notifyDataSetChanged();
  }

  @Override public void showMessage(String message) {
    new Snackbar(recyclerView).show(message);
  }

  @Override public void showPlaylist(PlayList playList) {
    getActivity().getSupportFragmentManager()
        .beginTransaction()
        .add(android.R.id.content, MembersFragment.newInstance(playList),
            MembersFragment.class.getSimpleName())
        .addToBackStack(MembersFragment.class.getSimpleName())
        .commit();
  }
}
