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
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.coverarts.CoverArtsFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class AlbumActivity extends BaseActivity<Album.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, Album.View {

  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.songMenu) SongListRayMenu songListRayMenu;
  @BindView(R.id.albumart) ImageView albumArtView;
  @BindView(R.id.totaltime) TextView totalTimeView;
  @BindView(R.id.totaltracks) TextView totalTracks;
  @BindView(R.id.artistname) TextView artistNameView;
  @BindView(R.id.rootView) View rootView;
  MusicRecyclerViewAdapter adapter;
  List<Music> songList = new ArrayList<>();
  private com.kingbull.musicplayer.domain.Album album;

  @OnClick(R.id.albumart) void onCoverArtClick() {
    getSupportFragmentManager().beginTransaction()
        .add(android.R.id.content, CoverArtsFragment.newInstanceOfAlbumCovers(album),
            CoverArtsFragment.class.getSimpleName())
        .addToBackStack(CoverArtsFragment.class.getSimpleName())
        .commit();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_album);
    ButterKnife.bind(this);
    album = getIntent().getParcelableExtra("album");
    adapter = new MusicRecyclerViewAdapter(songList, this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    getSupportLoaderManager().initLoader(0, null, this);
    titleView.setText(album.name());
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
    if (TextUtils.isEmpty(album.albumArt())) {
      Glide.with(this).load(R.drawable.a11).asBitmap().into(new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
          albumArtView.setImageBitmap(bitmap);
          rootView.setBackground(new ImagePath("").toBlurredBitmap(bitmap, getResources()));
        }
      });
    } else {
      Glide.with(this).load(album.albumArt()).asBitmap().into(new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
          albumArtView.setImageBitmap(bitmap);
          rootView.setBackground(new ImagePath("").toBlurredBitmap(bitmap, getResources()));
        }
      });
    }
  }

  @Override protected void onPresenterPrepared(Album.Presenter presenter) {
    presenter.takeView(this);
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.Album();
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AlbumSongsCursorLoader(this, album.albumId());
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
    if (!songs.isEmpty()) {
      artistNameView.setText(String.format("By: %s", songs.get(0).media().artist()));
    }
    totalTracks.setText(String.format("Total Tracks: %d", songs.size()));
  }

  @Override public void showTotalDuration(String duration) {
    totalTimeView.setText(String.format("Total Time: %s", duration));
  }
}
