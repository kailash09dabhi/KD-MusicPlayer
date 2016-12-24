package com.kingbull.musicplayer.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.kingbull.musicplayer.ui.main.categories.all.AllSongsFragment;
import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesFragment;
import com.kingbull.musicplayer.ui.main.categories.playlists.PlayListsFragment;
import com.kingbull.musicplayer.ui.settings.SettingsFragment;

final class MainPagerAdapter extends FragmentPagerAdapter {
  Context mContext;
  private String[] tabs;

  public MainPagerAdapter(FragmentManager fm, Context context, String[] tabs) {
    super(fm);
    mContext = context;
    this.tabs = tabs;
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment = null;
    if (position == 0) {
      fragment = new AllSongsFragment();
    } else if (position == 1) {
      fragment = new MusicCategoryFragment();
    } else if (position == 2) {
      fragment = new PlayListsFragment();
    } else if (position == 3) {
      fragment = new MyFilesFragment();
    } else if (position == 4) {
      fragment = new SettingsFragment();
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