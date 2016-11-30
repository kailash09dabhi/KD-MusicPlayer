package com.kingbull.musicplayer.ui.songlist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.Presenter;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import java.util.ArrayList;
import java.util.List;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;

import static com.kingbull.musicplayer.R.id.pager;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class SongListActivity extends BaseActivity
    implements LoaderManager.LoaderCallbacks<Cursor>, SongList.View {

  public static final String GENRE_ID = "genre_id";
  public static final int INT_GENRE_ID = 1;
  public static final int INT_ARTIST_ID = 2;
  public static final int INT_ALBUM_ID = 3;
  public static final String ARTIST_ID = "artist_id";
  public static final String ALBUM_ID = "album_id";
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.pager_container) PagerContainer pagerContainer;
  SongList.Presenter songListPresenter = new SongListPresenter();
  SongsAdapter adapter;
  List<Music> songList = new ArrayList<>();
  @BindView(pager) ViewPager viewPager;

  PagerAdapter pagerAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_list);
    ButterKnife.bind(this);
    //viewPager.setPageTransformer(true, new CustPagerTransformer(this));
    viewPager.setOffscreenPageLimit(3);
    //viewPager.setClipChildren(false);
    //viewPager.setCurrentItem(0);
    //mCoverFlow.setAdapter(mAdapter);
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override public void onPageSelected(int position) {
        songListPresenter.onAlbumSelected(position);
      }

      @Override public void onPageScrollStateChanged(int state) {
      }
    });
    new CoverFlow.Builder().with(viewPager)
        .scale(0.16f)
        .pagerMargin(-40f)
        .spaceSize(-40f)
        .rotationY(25f)
        .build();
    adapter = new SongsAdapter(songList);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    if (getIntent().hasExtra(GENRE_ID)) {
      getSupportLoaderManager().initLoader(INT_GENRE_ID, null, this);
    } else if (getIntent().hasExtra(ARTIST_ID)) {
      getSupportLoaderManager().initLoader(INT_ARTIST_ID, null, this);
    }else if (getIntent().hasExtra(ALBUM_ID)){
      getSupportLoaderManager().initLoader(INT_ALBUM_ID, null, this);
    }
    titleView.setText(getIntent().getStringExtra("title"));
    titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    titleView.setSingleLine(true);
    titleView.setMarqueeRepeatLimit(-1);
    titleView.setSelected(true);
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
    viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this, songs));
  }
}
