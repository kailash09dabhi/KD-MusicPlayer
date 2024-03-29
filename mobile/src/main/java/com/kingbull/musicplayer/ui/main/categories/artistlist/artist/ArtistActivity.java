package com.kingbull.musicplayer.ui.main.categories.artistlist.artist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.StatusBarColor;
import com.kingbull.musicplayer.ui.base.ads.AdmobBannerLoaded;
import com.kingbull.musicplayer.ui.base.analytics.Analytics;
import com.kingbull.musicplayer.ui.base.animators.Alpha;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.base.musiclist.OnSelectionListener;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import com.kingbull.musicplayer.ui.base.view.SelectionOptionsLayout;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import com.kingbull.musicplayer.ui.base.view.SnappingRecyclerView;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import com.kingbull.musicplayer.ui.sorted.SortDialogFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class ArtistActivity extends BaseActivity<Artist.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, Artist.View {
  private final Alpha.Animation alphaAnimation = new Alpha.Animation();
  private final List<Music> songList = new ArrayList<>();
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.selectionContextOptionsLayout) SelectionOptionsLayout selectionOptionsLayout;
  @BindView(R.id.coverRecyclerView) SnappingRecyclerView coverRecyclerView;
  @BindView(R.id.buttonLayout) LinearLayout buttonLayout;
  @BindView(R.id.totalSongCountView) TextView totalSongsView;
  @BindView(R.id.sortButton) ImageView sortButton;
  @BindView(R.id.shuffleButton) ImageView shuffleButton;
  @Inject Analytics analytics;
  @Inject ColorTheme.Transparent transparentTheme;
  private MusicRecyclerViewAdapter adapter;
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
    MusicPlayerApp.instance().component().inject(this);
    new AdmobBannerLoaded((ViewGroup) findViewById(android.R.id.content));
    artist = getIntent().getParcelableExtra("artist");
    coverRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    coverRecyclerView.setOnViewSelectedListener((view, position) -> presenter.onAlbumSelected(position));
    initializeWithThemeColors();
    coverRecyclerView.setHasFixedSize(true);
    adapter = new MusicRecyclerViewAdapter(songList, this);
    adapter.addOnSelectionListener(new OnSelectionListener() {
      @Override public void onClearSelection() {
        presenter.onClearSelection();
      }

      @Override public void onMultiSelection(int selectionCount) {
        presenter.onMultiSelection(selectionCount);
      }
    });
    selectionOptionsLayout.addOnContextOptionClickListener(
        new SelectionOptionsLayout.OnContextOptionClickListener() {
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
    sortButton.setImageDrawable(new IconDrawable(R.drawable.ic_sort_48dp, fillColor));
    shuffleButton.setImageDrawable(new IconDrawable(R.drawable.ic_shuffle_48dp, fillColor));
    selectionOptionsLayout.updateIconsColor(fillColor);
    selectionOptionsLayout.updateIconSize(IconDrawable.dpToPx(40));
    analytics.logScreen(ArtistActivity.class.getSimpleName());
  }

  private void initializeWithThemeColors() {
    new StatusBarColor(flatTheme.statusBar()).applyOn(getWindow());
    int headerColor = flatTheme.header().intValue();
    int screenColor = flatTheme.screen().intValue();
    ((View) titleView.getParent()).setBackgroundColor(headerColor);
    ((View) coverRecyclerView.getParent()).setBackgroundColor(screenColor);
    buttonLayout.setBackgroundColor(flatTheme.screen().transparent(0.16f).intValue());
    recyclerView.setBackgroundColor(transparentTheme.header().intValue());
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
        .subscribe(o -> {
          if (o instanceof SortEvent) {
            presenter.onSortEvent((SortEvent) o);
          }
        });
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new ArtistMusicsCursorLoader(this, artist.artistId(), settingPreferences);
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
            settingPreferences.filterDurationInSeconds()));
  }

  @Override public void showSelectionOptions() {
    alphaAnimation.fadeOut(titleView);
    alphaAnimation.fadeIn(selectionOptionsLayout);
  }

  @Override public void clearSelection() {
    adapter.clearSelection();
  }

  @Override public void hideSelectionOptions() {
    alphaAnimation.fadeOut(selectionOptionsLayout);
    alphaAnimation.fadeIn(titleView);
  }

  @Override public List<Music> selectedMusicList() {
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
    alphaAnimation.fadeOut(selectionOptionsLayout);
    alphaAnimation.fadeIn(titleView);
  }

  @Override public void showAddToPlayListDialog() {
    AddToPlayListDialogFragment.newInstance(adapter.getSelectedMusics())
        .show(getSupportFragmentManager(), AddToPlayListDialogFragment.class.getName());
  }

  @Override public void showSortMusicListDialog() {
    SortDialogFragment.newInstance()
        .show(getSupportFragmentManager(), SortDialogFragment.class.getName());
  }

  @Override public void showMusicScreen() {
    startActivity(new Intent(this, MusicPlayerActivity.class));
  }
}
