package com.kingbull.musicplayer.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.kingbull.musicplayer.ui.main.categories.albumlist.AlbumListFragment;
import com.kingbull.musicplayer.ui.main.categories.all.AllSongsFragment;
import com.kingbull.musicplayer.ui.main.categories.artists.ArtistFragment;
import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesFragment;
import com.kingbull.musicplayer.ui.main.categories.genres.GenresFragment;
import com.kingbull.musicplayer.ui.main.categories.playlists.PlayListsFragment;

final class PagerAdapter extends FragmentPagerAdapter {

  Context mContext;
  private String[] tabs;

  public PagerAdapter(FragmentManager fm, Context context, String[] tabs) {
    super(fm);
    mContext = context;
    this.tabs = tabs;
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment;
    if (position == 0) {
      fragment = new GenresFragment();
    } else if (position == 1) {
      fragment = new PlayListsFragment();
    } else if (position == 2) {
      fragment = new AllSongsFragment();
    } else if (position == 3) {
      fragment = new ArtistFragment();
    } else if (position == 4) {
      fragment = new AlbumListFragment();
    } else if (position == 5) {
      fragment = new MyFilesFragment();
    } else {
      fragment = new ArtistFragment();
    }
    Bundle args = new Bundle();
    args.putInt("page_position", position + 1);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public int getCount() {
    return tabs.length;
  }

  @Override public CharSequence getPageTitle(int position) {
    return tabs[position];
  }
}