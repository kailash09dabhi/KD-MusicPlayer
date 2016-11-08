package com.kingbull.musicplayer.ui.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kingbull.musicplayer.R;

public class MainActivity extends FragmentActivity {

  CustomPagerAdapter mCustomPagerAdapter;
  ViewPager mViewPager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    String[] tabs = getResources().getStringArray(R.array.tabs);
    mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this, tabs);
    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mCustomPagerAdapter);
    PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.tabTitleStrip);
    Typeface localTypeface = Typeface.createFromAsset(getAssets(), "fonts/julius-sans-one.ttf");
    for (int i1 = 0; i1 < pagerTitleStrip.getChildCount(); i1++) {
      if ((pagerTitleStrip.getChildAt(i1) instanceof TextView)) {
        ((TextView) pagerTitleStrip.getChildAt(i1)).setTypeface(localTypeface);
      }
    }
  }

  public static class DemoFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.item_song_type, container, false);
      Bundle args = getArguments();
      ((TextView) rootView.findViewById(R.id.text_view1)).setText(
          "Page " + args.getInt("page_position"));
      return rootView;
    }
  }

}