package com.kingbull.musicplayer.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.kingbull.musicplayer.ui.main.songgroup.album.AlbumFragment;
import com.kingbull.musicplayer.ui.main.songgroup.genres.GenresFragment;

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
    } else {
      fragment = new AlbumFragment();
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