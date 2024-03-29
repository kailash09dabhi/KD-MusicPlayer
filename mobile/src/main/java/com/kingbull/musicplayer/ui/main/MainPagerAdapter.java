package com.kingbull.musicplayer.ui.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.kingbull.musicplayer.ui.main.categories.all.AllSongsFragment;
import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesFragment;
import com.kingbull.musicplayer.ui.main.categories.playlists.AllPlayListFragment;
import com.kingbull.musicplayer.ui.settings.SettingsFragment;

final class MainPagerAdapter extends FragmentPagerAdapter {
  private final String[] tabs;

  public MainPagerAdapter(FragmentManager fm, String[] tabs) {
    super(fm);
    this.tabs = tabs;
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment = null;
    if (position == 0) {
      fragment = new AllSongsFragment();
    } else if (position == 1) {
      fragment = new MusicCategoryFragment();
    } else if (position == 2) {
      fragment = new AllPlayListFragment();
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