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
import android.support.v7.app.AppCompatActivity;
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
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import com.kingbull.musicplayer.ui.settings.SettingsActivity;
import com.kingbull.musicplayer.ui.sorted.SortDialogFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;

public final class AllSongsFragment extends BaseFragment<AllSongs.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, AllSongs.View {
  private final List<Music> musicList = new ArrayList<>();
  @BindView(R.id.totalSongCountView) TextView totalSongCountView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.allRayMenu) AllRayMenu allRayMenu;
  @BindView(R.id.searchView) EditText searchView;
  CompositeDisposable compositeDisposable = new CompositeDisposable();
  private MusicRecyclerViewAdapter musicRecyclerViewAdapter;

  @OnTextChanged(R.id.searchView) void onSearchTextChanged(CharSequence text) {
    presenter.onSearchTextChanged(text.toString());
  }

  @OnClick(R.id.exitSearchView) void onExitSearchClick() {
    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
    animation.setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {
      }

      @Override public void onAnimationEnd(Animation animation) {
        allRayMenu.setVisibility(View.VISIBLE);
        Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        animation1.setDuration(300);
        animation1.setInterpolator(new OvershootInterpolator(1.5f));
        animation1.setAnimationListener(new Animation.AnimationListener() {
          @Override public void onAnimationStart(Animation animation) {
          }

          @Override public void onAnimationEnd(Animation animation) {
            allRayMenu.expand(true);
          }

          @Override public void onAnimationRepeat(Animation animation) {
          }
        });
        allRayMenu.startAnimation(animation1);
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
    musicRecyclerViewAdapter = new MusicRecyclerViewAdapter(musicList,
        (AppCompatActivity) getActivity());
    recyclerView.setAdapter(musicRecyclerViewAdapter);
    allRayMenu.post(new Runnable() {
      @Override public void run() {
        searchView.getLayoutParams().height = allRayMenu.getHeight();
      }
    });
    allRayMenu.addOnMenuClickListener(new AllRayMenu.OnMenuClickListener() {
      @Override public void onSearchMenuClick() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
        animation.setAnimationListener(new Animation.AnimationListener() {
          @Override public void onAnimationStart(Animation animation) {
          }

          @Override public void onAnimationEnd(Animation animation) {
            allRayMenu.setVisibility(View.GONE);
            searchView.startAnimation(
                AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
          }

          @Override public void onAnimationRepeat(Animation animation) {
          }
        });
        animation.setInterpolator(new OvershootInterpolator(1.5f));
        allRayMenu.startAnimation(animation);
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

      @Override public void onSortMenuClick() {
        presenter.onSortMenuClick();
      }
    });
    compositeDisposable.add(RxBus.getInstance()
        .toObservable()
        .ofType(SortEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<SortEvent>() {
          @Override public void accept(SortEvent sortEvent) {
            presenter.onSortEvent(sortEvent);
          }
        }));
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (compositeDisposable != null) {
      compositeDisposable.clear();
    }
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

  @Override protected void onPresenterPrepared(AllSongs.Presenter presenter) {
    ButterKnife.bind(this, getView());
    setupView();
    presenter.takeView(this);
  }

  @Override protected PresenterFactory<AllSongs.Presenter> presenterFactory() {
    return new PresenterFactory.AllSongs();
  }
}
