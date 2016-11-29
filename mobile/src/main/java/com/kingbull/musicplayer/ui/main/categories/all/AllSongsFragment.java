/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.categories.all;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import com.kingbull.musicplayer.ui.settings.SettingsActivity;
import java.util.ArrayList;
import java.util.List;

public final class AllSongsFragment extends BaseFragment<AllSongs.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, AllSongs.View {
  private final List<Music> musicList = new ArrayList<>();
  @BindView(R.id.totalSongCountView) TextView totalSongCountView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.songMenu) SongMenu songMenu;
  @BindView(R.id.searchView) EditText searchView;
  private com.kingbull.musicplayer.ui.main.categories.all.SongsAdapter songsAdapter;

  @OnTextChanged(R.id.searchView) void onSearchTextChanged(CharSequence text) {
    presenter.onSearchTextChanged(text.toString());
  }

  @OnClick(R.id.exitSearchView) void onExitSearchClick() {
    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
    animation.setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {
      }

      @Override public void onAnimationEnd(Animation animation) {
        songMenu.setVisibility(View.VISIBLE);
        Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        animation1.setDuration(300);
        animation1.setInterpolator(new OvershootInterpolator(1.5f));
        animation1.setAnimationListener(new Animation.AnimationListener() {
          @Override public void onAnimationStart(Animation animation) {
          }

          @Override public void onAnimationEnd(Animation animation) {
            songMenu.expand(true);
          }

          @Override public void onAnimationRepeat(Animation animation) {
          }
        });
        songMenu.startAnimation(animation1);
      }

      @Override public void onAnimationRepeat(Animation animation) {
      }
    });
    ((View) searchView.getParent()).startAnimation(animation);
    presenter.onExitSearchClick();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_all_songs, null);
  }

  private void setupView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    getLoaderManager().initLoader(0, null, this);
    songsAdapter = new com.kingbull.musicplayer.ui.main.categories.all.SongsAdapter(musicList);
    recyclerView.setAdapter(songsAdapter);
    songMenu.post(new Runnable() {
      @Override public void run() {
        searchView.getLayoutParams().height = songMenu.getHeight();
      }
    });
    songMenu.addOnMenuClickListener(new SongMenu.OnMenuClickListener() {
      @Override public void onSearchMenuClick() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
        animation.setAnimationListener(new Animation.AnimationListener() {
          @Override public void onAnimationStart(Animation animation) {
          }

          @Override public void onAnimationEnd(Animation animation) {
            songMenu.setVisibility(View.GONE);
            searchView.startAnimation(
                AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
          }

          @Override public void onAnimationRepeat(Animation animation) {
          }
        });
        animation.setInterpolator(new OvershootInterpolator(1.5f));
        songMenu.startAnimation(animation);
        //searchView.startAnimation(animation);
      }

      @Override public void onShuffleMenuClick() {
        presenter.onShuffleMenuClick();
      }

      @Override public void onSettingsMenuClick() {
        presenter.onSettingsMenuClick();
      }

      @Override public void onAddToPlaylistMenuClick() {
        presenter.onAddToPlayListMenuClick();
      }
    });
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AllSongsCursorLoader(getContext());
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    presenter.onAllSongsCursorLoadFinished(cursor);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override public void showAllSongs(List<Music> songs) {
    totalSongCountView.setText(String.valueOf(songs.size()));
    musicList.clear();
    musicList.addAll(songs);
    songsAdapter.notifyDataSetChanged();
  }

  @Override public void showMusicScreen() {
    startActivity(new Intent(getActivity(), MusicPlayerActivity.class));
  }

  @Override public void showSettingsScreen() {
    startActivity(new Intent(getActivity(), SettingsActivity.class));
  }

  @Override public void showAddToPlayListDialog() {
    AddToPlayListDialogFragment.newInstance(songsAdapter.getSelectedMusics())
        .show(getActivity().getSupportFragmentManager(),
            AddToPlayListDialogFragment.class.getName());
    songsAdapter.clearSelection();
  }

  @Override protected void onPresenterPrepared(AllSongs.Presenter presenter) {
    ButterKnife.bind(this, getView());
    setupView();
    presenter.takeView(this);
  }

  @Override protected PresenterFactory<AllSongs.Presenter> presenterFactory() {
    return new PresenterFactory.AllSongs();
  }
}
