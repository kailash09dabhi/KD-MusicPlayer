package com.kingbull.musicplayer.ui.main.categories.artistlist.artist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.StatusBarColor;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import com.kingbull.musicplayer.ui.base.view.SnappingRecyclerView;
import com.kingbull.musicplayer.ui.main.categories.genreslist.genre.SongListRayMenu;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class ArtistActivity extends BaseActivity<Artist.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, Artist.View {
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.coverRecyclerView) SnappingRecyclerView coverRecyclerView;
  @BindView(R.id.songMenu) SongListRayMenu artistRayMenu;
  MusicRecyclerViewAdapter adapter;
  List<Music> songList = new ArrayList<>();
  com.kingbull.musicplayer.domain.Artist artist;

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
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    getSupportLoaderManager().initLoader(0, null, this);
    titleView.setText(artist.name());
    titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    titleView.setSingleLine(true);
    titleView.setMarqueeRepeatLimit(-1);
    titleView.setSelected(true);
    artistRayMenu.addOnMenuClickListener(new SongListRayMenu.OnMenuClickListener() {
      @Override public void onShuffleMenuClick() {
        //presenter.onShuffleMenuClick();
      }

      @Override public void onAddToPlaylistMenuClick() {
        //presenter.onAddToPlayListMenuClick();
      }

      @Override public void onSortMenuClick() {
        //presenter.onSortMenuClick();
      }
    });
  }

  private void initializeWithThemeColors() {
    ColorTheme.Flat colorTheme = new ColorTheme.Flat();
    com.kingbull.musicplayer.ui.base.Color color =
        new com.kingbull.musicplayer.ui.base.Color(colorTheme.statusBar().intValue());
    new StatusBarColor(color).applyOn(getWindow());
    int headerColor = colorTheme.header().intValue();
    titleView.setBackgroundColor(headerColor);
    recyclerView.setBackgroundColor(headerColor);
    coverRecyclerView.setBackgroundColor(colorTheme.screen().intValue());
    artistRayMenu.setBackgroundColor(colorTheme.screen().intValue());
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.Artist();
  }

  @Override protected void onPresenterPrepared(Artist.Presenter presenter) {
    presenter.takeView(this);
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
}
