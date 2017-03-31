/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.categories.all;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jaredrummler.fastscrollrecyclerview.FastScrollRecyclerView;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.event.DurationFilterEvent;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.UiColors;
import com.kingbull.musicplayer.ui.base.animators.Alpha;
import com.kingbull.musicplayer.ui.base.animators.SlideHorizontal;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import com.kingbull.musicplayer.ui.settings.SettingsActivity;
import com.kingbull.musicplayer.ui.sorted.SortDialogFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class AllSongsFragment extends BaseFragment<AllSongs.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, AllSongs.View {
  private final List<Music> musicList = new ArrayList<>();
  private final Alpha.Animation alphaAnimation = new Alpha.Animation();
  private final SlideHorizontal.Animation slideAnimation = new SlideHorizontal.Animation();
  @BindView(R.id.totalSongCountView) TextView totalSongCountView;
  @BindView(R.id.recyclerView) FastScrollRecyclerView recyclerView;
  @BindView(R.id.allRayMenu) AllRayMenu allRayMenu;
  @BindView(R.id.totalSongLayout) LinearLayout totalSongLayout;
  @BindView(R.id.searchLayout) LinearLayout searchLayout;
  @BindView(R.id.selectionContextOptionsLayout) SelectionContextOptionsLayout
      selectionContextOptionsLayout;
  private final SlideHorizontal.Listener.Default slideExitSearchListener =
      new SlideHorizontal.Listener.Default() {
        @Override public void onOutAnimationFinished() {
          selectionContextOptionsLayout.setVisibility(View.GONE);
          searchLayout.setVisibility(View.GONE);
          alphaAnimation.animateIn(totalSongLayout, Alpha.Listener.NONE);
        }
      };
  @BindView(R.id.searchView) EditText searchView;
  private final SlideHorizontal.Listener.Default slideAllRayMenuAnimationListener =
      new SlideHorizontal.Listener.Default() {
        @Override public void onOutAnimationFinished() {
          allRayMenu.setVisibility(View.GONE);
          searchView.startAnimation(
              AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
        }
      };
  private MusicRecyclerViewAdapter musicRecyclerViewAdapter;

  @OnTextChanged(R.id.searchView) void onSearchTextChanged(CharSequence text) {
    presenter.onSearchTextChanged(text.toString());
  }

  @OnClick(R.id.exitSearchView) void onExitSearchClick() {
    slideAnimation.animateOut(searchLayout, slideExitSearchListener);
    presenter.onExitSearchClick();
  }

  @OnClick(R.id.searchButton) void onSearchClick() {
    alphaAnimation.animateOut(totalSongLayout, Alpha.Listener.NONE);
    slideAnimation.animateIn(searchLayout, SlideHorizontal.Listener.NONE);
  }

  @OnClick(R.id.sortButton) void onSortClick() {
    presenter.onSortMenuClick();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_all_songs, null);
  }

  private void setupView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    UiColors uiColors = new UiColors();
    int popupColor = uiColors.statusBar().intValue();
    recyclerView.setPopupBackgroundColor(Color.WHITE);
    recyclerView.setThumbActiveColor(Color.WHITE);
    recyclerView.setTrackInactiveColor(popupColor);
    recyclerView.setPopupTextColor(popupColor);
    int screenColor = uiColors.screen().intValue();
    recyclerView.setBackgroundColor(screenColor);
    ((View) totalSongLayout.getParent()).setBackgroundColor(uiColors.tab().intValue());
    allRayMenu.setBackgroundColor(screenColor);
    getLoaderManager().initLoader(0, null, this);
    musicRecyclerViewAdapter =
        new MusicRecyclerViewAdapter(musicList, (AppCompatActivity) getActivity());
    recyclerView.setAdapter(musicRecyclerViewAdapter);
    musicRecyclerViewAdapter.addOnLongClickListener(
        new MusicRecyclerViewAdapter.OnLongClickListener() {
          @Override public void onLongClick() {
            alphaAnimation.animateOut(totalSongLayout, Alpha.Listener.NONE);
            alphaAnimation.animateIn(selectionContextOptionsLayout, Alpha.Listener.NONE);
          }
        });
    selectionContextOptionsLayout.addOnContextOptionClickListener(
        new SelectionContextOptionsLayout.OnContextOptionClickListener() {
          @Override public void onAddToPlaylistClick() {
            presenter.onAddToPlayListMenuClick();
          }

          @Override public void onDeleteSelectedClick() {
            presenter.onDeleteSelectedMusic();
          }

          @Override public void onClearSelectionClick() {
            musicRecyclerViewAdapter.clearSelection();
            alphaAnimation.animateOut(selectionContextOptionsLayout, Alpha.Listener.NONE);
            alphaAnimation.animateIn(totalSongLayout, Alpha.Listener.NONE);
          }
        });
    allRayMenu.addOnMenuClickListener(new AllRayMenu.OnMenuClickListener() {
      @Override public void onSearchMenuClick() {
        slideAnimation.animateOut(allRayMenu, new SlideHorizontal.Listener.Default() {
          @Override public void onOutAnimationFinished() {
            allRayMenu.setVisibility(View.GONE);
            searchView.startAnimation(
                AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
          }
        });
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

      @Override public void onSortMenuClick() {
        presenter.onSortMenuClick();
      }
    });
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof SortEvent) {
              presenter.onSortEvent((SortEvent) o);
            } else if (o instanceof DurationFilterEvent) {
              getLoaderManager().restartLoader(0, null, AllSongsFragment.this);
            }
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
    musicRecyclerViewAdapter.notifyDataSetChanged();
  }

  @Override public void showMusicScreen() {
    startActivity(new Intent(getActivity(), MusicPlayerActivity.class));
  }

  @Override public void showSettingsScreen() {
    startActivity(new Intent(getActivity(), SettingsActivity.class));
  }

  @Override public void showAddToPlayListDialog() {
    AddToPlayListDialogFragment.newInstance(musicRecyclerViewAdapter.getSelectedMusics())
        .show(getActivity().getSupportFragmentManager(),
            AddToPlayListDialogFragment.class.getName());
    musicRecyclerViewAdapter.clearSelection();
  }

  @Override public void showSortMusicScreen() {
    new SortDialogFragment().show(getActivity().getSupportFragmentManager(),
        SortDialogFragment.class.getName());
  }

  @Override public List<SqlMusic> selectedMusicList() {
    return musicRecyclerViewAdapter.getSelectedMusics();
  }

  @Override public void removeFromList(Music music) {
    new Snackbar(recyclerView).show("songs deleted successfully!");
    musicRecyclerViewAdapter.notifyItemRemoved(musicList.indexOf(music));
    musicList.remove(music);
  }

  @Override public void clearSelection() {
    musicRecyclerViewAdapter.clearSelection();
  }

  @Override public void removeSongFromMediaStore(Music music) {
    getActivity().getContentResolver()
        .delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.MediaColumns.DATA + "=?",
            new String[] { music.media().path() });
    getActivity().sendBroadcast(
        new Intent(Intent.ACTION_DELETE, Uri.fromFile(new File(music.media().path()))));
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
