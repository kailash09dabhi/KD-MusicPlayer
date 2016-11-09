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
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class SongListActivity extends BaseActivity
    implements LoaderManager.LoaderCallbacks<Cursor>, SongList.View {

  @BindView(R.id.recyclerview_grid) RecyclerView recyclerView;
  SongList.Presenter songListPresenter = new SongListPresenter();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_list);
    ButterKnife.bind(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    getSupportLoaderManager().initLoader(getIntent().getIntExtra("genre_id", 0), null, this);
    TextView titleView = (TextView) this.findViewById(R.id.titleView);
    titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    titleView.setSingleLine(true);
    titleView.setMarqueeRepeatLimit(-1);
    titleView.setSelected(true);
    songListPresenter.takeView(this);
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new SongListCursorLoader(this, id);
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    songListPresenter.onSongCursorLoadFinished(data);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override public void showSongs(List<Song> songs) {
    recyclerView.setAdapter(new SongsAdapter(songs));
  }
}
