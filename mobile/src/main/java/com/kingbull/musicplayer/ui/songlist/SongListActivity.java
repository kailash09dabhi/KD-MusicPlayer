package com.kingbull.musicplayer.ui.songlist;

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
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.Presenter;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class SongListActivity extends BaseActivity
    implements LoaderManager.LoaderCallbacks<Cursor>, SongList.View {

  public static final int INT_GENRE_ID = 1;
  public static final int INT_ARTIST_ID = 2;
  public static final int INT_ALBUM_ID = 3;
  public static final String GENRE_ID = "genre_id";
  public static final String ARTIST_ID = "artist_id";
  public static final String ALBUM_ID = "album_id";
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.coverRecyclerView) SnappingRecyclerView coverRecyclerView;
  @BindView(R.id.songMenu) SongListRayMenu songListRayMenu;
  SongList.Presenter songListPresenter = new SongListPresenter();
  SongsAdapter adapter;
  List<Music> songList = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_list);
    ButterKnife.bind(this);
    coverRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    coverRecyclerView.setOnViewSelectedListener(new SnappingRecyclerView.OnViewSelectedListener() {
      @Override public void onSelected(View view, int position) {
        songListPresenter.onAlbumSelected(position);
      }
    });
    coverRecyclerView.setHasFixedSize(true);
    adapter = new SongsAdapter(songList);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    if (getIntent().hasExtra(GENRE_ID)) {
      getSupportLoaderManager().initLoader(INT_GENRE_ID, null, this);
    } else if (getIntent().hasExtra(ARTIST_ID)) {
      getSupportLoaderManager().initLoader(INT_ARTIST_ID, null, this);
    } else if (getIntent().hasExtra(ALBUM_ID)) {
      getSupportLoaderManager().initLoader(INT_ALBUM_ID, null, this);
    }
    titleView.setText(getIntent().getStringExtra("title"));
    titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    titleView.setSingleLine(true);
    titleView.setMarqueeRepeatLimit(-1);
    titleView.setSelected(true);
    songListRayMenu.addOnMenuClickListener(new SongListRayMenu.OnMenuClickListener() {

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

  @Override protected void onPresenterPrepared(Presenter presenter) {
    songListPresenter.takeView(this);
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.SongList();
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (INT_GENRE_ID == id) {
      return SongListCursorLoader.instance(this, getIntent().getIntExtra(GENRE_ID, 0), GENRE_ID);
    } else if (id == INT_ARTIST_ID) {
      return SongListCursorLoader.instance(this, getIntent().getIntExtra(ARTIST_ID, 0), ARTIST_ID);
    }
    return SongListCursorLoader.instance(this, getIntent().getIntExtra(ALBUM_ID, 0), ALBUM_ID);
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    songListPresenter.onSongCursorLoadFinished(data);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override public void showSongs(List<Music> songs) {
    songList.clear();
    songList.addAll(songs);
    adapter.notifyDataSetChanged();
  }

  @Override public void setAlbumPager(Music[] songs) {
    coverRecyclerView.setAdapter(new CoverAdapter(Arrays.asList(songs)));
  }
}
