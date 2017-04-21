package com.kingbull.musicplayer.ui.main.categories.artistlist.artist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.StatusBarColor;
import com.kingbull.musicplayer.ui.base.animators.Alpha;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import com.kingbull.musicplayer.ui.base.view.SnappingRecyclerView;
import com.kingbull.musicplayer.ui.main.categories.all.SelectionContextOptionsLayout;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import com.kingbull.musicplayer.ui.sorted.SortDialogFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class ArtistActivity extends BaseActivity<Artist.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, Artist.View {
  private final Alpha.Animation alphaAnimation = new Alpha.Animation();
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.selectionContextOptionsLayout) SelectionContextOptionsLayout
      selectionContextOptionsLayout;
  @BindView(R.id.coverRecyclerView) SnappingRecyclerView coverRecyclerView;
  @BindView(R.id.buttonLayout) LinearLayout buttonLayout;
  @BindView(R.id.totalSongCountView) TextView totalSongsView;
  @BindView(R.id.sortButton) ImageView sortButton;
  @BindView(R.id.shuffleButton) ImageView shuffleButton;
  private MusicRecyclerViewAdapter adapter;
  private List<Music> songList = new ArrayList<>();
  private com.kingbull.musicplayer.domain.Artist artist;

  @OnClick(R.id.sortButton) void onSortClick() {
    presenter.onSortMenuClick();
  }

  @OnClick(R.id.shuffleButton) void onShuffleClick() {
    presenter.onShuffleMenuClick();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_list);
    ButterKnife.bind(this);
    artist = getIntent().getParcelableExtra("artist");
    coverRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    coverRecyclerView.setOnViewSelectedListener(new SnappingRecyclerView.OnViewSelectedListener() {
      @Override public void onSelected(View view, int position) {
        presenter.onAlbumSelected(position);
      }
    });
    initializeWithThemeColors();
    coverRecyclerView.setHasFixedSize(true);
    adapter = new MusicRecyclerViewAdapter(songList, this);
    adapter.addOnSelectionListener(new MusicRecyclerViewAdapter.OnSelectionListener() {
      @Override public void onClearSelection() {
        presenter.onClearSelection();
      }

      @Override public void onMultiSelection(int selectionCount) {
        presenter.onMultiSelection(selectionCount);
      }
    });
    selectionContextOptionsLayout.addOnContextOptionClickListener(
        new SelectionContextOptionsLayout.OnContextOptionClickListener() {
          @Override public void onAddToPlaylistClick() {
            presenter.onAddToPlayListMenuClick();
          }

          @Override public void onDeleteSelectedClick() {
            presenter.onDeleteSelectedMusicClick();
          }

          @Override public void onClearSelectionClick() {
            presenter.onClearSelectionClick();
          }
        });
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    getSupportLoaderManager().initLoader(0, null, this);
    titleView.setText(artist.name());
    titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    titleView.setSingleLine(true);
    titleView.setMarqueeRepeatLimit(-1);
    titleView.setSelected(true);
    int fillColor = 0;
    sortButton.setImageDrawable(new IconDrawable(R.drawable.ic_sort_48dp, Color.WHITE, fillColor));
    shuffleButton.setImageDrawable(
        new IconDrawable(R.drawable.ic_shuffle_48dp, Color.WHITE, fillColor));
    selectionContextOptionsLayout.updateIconsColor(fillColor);
    selectionContextOptionsLayout.updateIconSize(IconDrawable.dpToPx(40));
  }

  private void initializeWithThemeColors() {
    new StatusBarColor(flatTheme.statusBar()).applyOn(getWindow());
    int headerColor = flatTheme.header().intValue();
    int screenColor = flatTheme.screen().intValue();
    ((View) titleView.getParent()).setBackgroundColor(headerColor);
    ((View) coverRecyclerView.getParent()).setBackgroundColor(screenColor);
    buttonLayout.setBackgroundColor(flatTheme.screen().transparent(0.16f).intValue());
    recyclerView.setBackgroundColor(new ColorTheme.Transparent().header().intValue());
    ((View) recyclerView.getParent()).setBackgroundColor(headerColor);
  }

  @NonNull @Override protected PresenterFactory<Artist.Presenter> presenterFactory() {
    return new PresenterFactory.Artist();
  }

  @Override protected void onPresenterPrepared(Artist.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof SortEvent) {
              presenter.onSortEvent((SortEvent) o);
            }
          }
        });
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new ArtistMusicsCursorLoader(this, artist.artistId());
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    presenter.onSongCursorLoadFinished(data);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override public void showSongs(List<Music> songs) {
    songList.clear();
    songList.addAll(songs);
    adapter.notifyDataSetChanged();
    totalSongsView.setText(songs.size() + " songs");
  }

  @Override public void setAlbumPager(List<Album> songs) {
    coverRecyclerView.setAdapter(new AlbumAdapter(songs));
  }

  @Override public void showEmptyView() {
    new Snackbar(findViewById(android.R.id.content)).show(
        getString(R.string.error_message_couldnt_fetch_music));
  }

  @Override public void showEmptyDueToDurationFilterMessage() {
    new Snackbar(findViewById(android.R.id.content)).show(
        String.format(getString(R.string.message_empty_due_to_duration_filter),
            new SettingPreferences().filterDurationInSeconds()));
  }

  @Override public void showSelectionOptions() {
    alphaAnimation.animateOut(titleView, Alpha.Listener.NONE);
    alphaAnimation.animateIn(selectionContextOptionsLayout, Alpha.Listener.NONE);
  }

  @Override public void clearSelection() {
    adapter.clearSelection();
  }

  @Override public void hideSelectionOptions() {
    alphaAnimation.animateOut(selectionContextOptionsLayout, Alpha.Listener.NONE);
    alphaAnimation.animateIn(titleView, Alpha.Listener.NONE);
  }

  @Override public List<SqlMusic> selectedMusicList() {
    return adapter.getSelectedMusics();
  }

  @Override public void removeFromList(Music music) {
    adapter.notifyItemRemoved(songList.indexOf(music));
    songList.remove(music);
  }

  @Override public void showMessage(String message) {
    new Snackbar(recyclerView).show(message);
  }

  @Override public void hideSelectionContextOptions() {
    alphaAnimation.animateOut(selectionContextOptionsLayout, Alpha.Listener.NONE);
    alphaAnimation.animateIn(titleView, Alpha.Listener.NONE);
  }

  @Override public void showAddToPlayListDialog() {
    AddToPlayListDialogFragment.newInstance(adapter.getSelectedMusics())
        .show(getSupportFragmentManager(), AddToPlayListDialogFragment.class.getName());
  }

  @Override public void showSortMusicListDialog() {
    new SortDialogFragment().show(getSupportFragmentManager(), SortDialogFragment.class.getName());
  }

  @Override public void showMusicScreen() {
    startActivity(new Intent(this, MusicPlayerActivity.class));
  }
}
