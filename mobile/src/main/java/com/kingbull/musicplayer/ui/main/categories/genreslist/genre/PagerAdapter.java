package com.kingbull.musicplayer.ui.main.categories.genreslist.genre;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.kingbull.musicplayer.domain.Music;

final class PagerAdapter extends FragmentPagerAdapter {

  Context mContext;
  private Music[] tabs;

  public PagerAdapter(FragmentManager fm, Context context, Music[] tabs) {
    super(fm);
    mContext = context;
    this.tabs = tabs;
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment;
    fragment = PagerFragment.instance(tabs[position]);
    Bundle args = new Bundle();
    args.putInt("page_position", position + 1);
    fragment.setArguments(args);
    return fragment;
  }

  //@Override public float getPageWidth(int position) {
  //  return 0.44f;
  //}

  @Override public int getCount() {
    return tabs.length;
  }

  @Override public CharSequence getPageTitle(int position) {
    return tabs[position].media().album();
  }
}