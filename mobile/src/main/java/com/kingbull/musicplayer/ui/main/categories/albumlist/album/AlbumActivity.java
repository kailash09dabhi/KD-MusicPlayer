package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.base.Presenter;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class AlbumActivity extends BaseActivity
    implements LoaderManager.LoaderCallbacks<Cursor>, Album.View {

  public static final int INT_ALBUM_ID = 3;
  public static final String ALBUM_ID = "album_id";
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.songMenu) SongListRayMenu songListRayMenu;
  @BindView(R.id.albumart) ImageView albumArtView;
  @BindView(R.id.totaltime) TextView totalTimeView;
  @BindView(R.id.totaltracks) TextView totalTracks;
  @BindView(R.id.rootView) View rootView;
  Album.Presenter songListPresenter = new SongListPresenter();
  MusicRecyclerViewAdapter adapter;
  List<Music> songList = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_album);
    ButterKnife.bind(this);
    adapter = new MusicRecyclerViewAdapter(songList,this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    getSupportLoaderManager().initLoader(INT_ALBUM_ID, null, this);
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
    ImagePath imagePath = new ImagePath(getIntent().getStringExtra("albumart"));
    Bitmap bitmap = imagePath.toBitmap(getResources());
    albumArtView.setImageBitmap(bitmap);
    rootView.setBackground(imagePath.toBlurredBitmap(bitmap, getResources()));
  }

  @Override protected void onPresenterPrepared(Presenter presenter) {
    songListPresenter.takeView(this);
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.SongList();
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
    totalTracks.setText(String.format("Total Tracks: %d",songs.size()));
  }

  @Override public void showTotalDuration(String duration) {
    totalTimeView.setText(String.format("Total Time: %s", duration));
  }
}
