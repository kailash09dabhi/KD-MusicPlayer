package com.kingbull.musicplayer.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.kingbull.musicplayer.ui.main.categories.albumlist.AlbumListFragment;
import com.kingbull.musicplayer.ui.main.categories.artistlist.ArtistListFragment;
import com.kingbull.musicplayer.ui.main.categories.genreslist.GenresListFragment;

final class MusicCategoryPagerAdapter extends FragmentPagerAdapter {
  private final String[] tabs;

  public MusicCategoryPagerAdapter(FragmentManager fm, String[] tabs) {
    super(fm);
    this.tabs = tabs;
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment;
    if (position == 0) {
      fragment = new GenresListFragment();
    } else if (position == 1) {
      fragment = new ArtistListFragment();
    } else if (position == 2) {
      fragment = new AlbumListFragment();
    } else {
      fragment = new ArtistListFragment();
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
    return tabs[position].toUpperCase();
  }
}