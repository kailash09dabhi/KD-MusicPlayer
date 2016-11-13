package com.kingbull.musicplayer.ui.songlist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class SongListActivity extends BaseActivity
    implements LoaderManager.LoaderCallbacks<Cursor>, SongList.View {

  public static final String GENRE_ID = "genre_id";
  public static final String ARTIST_ID = "artist_id";
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.titleView) TextView titleView;
  SongList.Presenter songListPresenter = new SongListPresenter();
  SongsAdapter adapter;
  List<Song> songList = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_list);
    ButterKnife.bind(this);
    adapter = new SongsAdapter(songList);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    if (getIntent().hasExtra(GENRE_ID)) {
      getSupportLoaderManager().initLoader(getIntent().getIntExtra(GENRE_ID, 0), null, this);
    } else if (getIntent().hasExtra(ARTIST_ID)) {
      getSupportLoaderManager().initLoader(getIntent().getIntExtra(ARTIST_ID, 0), null, this);
    }
    titleView.setText(getIntent().getStringExtra("title"));
    titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    titleView.setSingleLine(true);
    titleView.setMarqueeRepeatLimit(-1);
    titleView.setSelected(true);
    songListPresenter.takeView(this);
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (getIntent().hasExtra(GENRE_ID)) {
      return SongListCursorLoader.instance(this, id, GENRE_ID);
    } else if (getIntent().hasExtra(ARTIST_ID)) {
      return SongListCursorLoader.instance(this, id, ARTIST_ID);
    } else {
      return SongListCursorLoader.instance(this, id, GENRE_ID);
    }
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    songListPresenter.onSongCursorLoadFinished(data);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override public void showSongs(List<Song> songs) {
    songList.clear();
    songList.addAll(songs);
    adapter.notifyDataSetChanged();
  }
}
