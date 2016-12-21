package com.kingbull.musicplayer.ui.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerTitleStrip;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.main.categories.playlists.musics.MusicListOfPlaylist;
import java.util.Random;

/**
 * @author Kailash Dabhi
 * @date 12/20/2016.
 */

public class MusicCategoryFragment extends BaseFragment<MusicListOfPlaylist.Presenter> {
  private final int arrayBg[] = {
      R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6,
      R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
      R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17,
      R.drawable.a18,
  };
  @BindView(R.id.pager) com.kingbull.musicplayer.ui.main.ViewPagerParallax viewPager;
  @BindView(R.id.tabTitleStrip) PagerTitleStrip pagerTitleStrip;
  @BindArray(R.array.tabs) String[] tabs;
  private MusicCategoryPagerAdapter musicCategoryPagerAdapter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_music_category, null);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    musicCategoryPagerAdapter =
        new MusicCategoryPagerAdapter(getChildFragmentManager(), getActivity(), tabs);
    viewPager.setAdapter(musicCategoryPagerAdapter);
    int i2 = new Random().nextInt(18);
    viewPager.setBackgroundAsset(arrayBg[i2], getActivity().getWindow());
    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    viewPager.setOffscreenPageLimit(3);
    viewPager.setCurrentItem(2);
    setupPagerTitleStrip();
  }

  @Override protected void onPresenterPrepared(MusicListOfPlaylist.Presenter presenter) {
  }

  @Override protected PresenterFactory<MusicListOfPlaylist.Presenter> presenterFactory() {
    return new PresenterFactory.MusicListOfPlaylist();
  }

  private void setupPagerTitleStrip() {
    Typeface localTypeface =
        Typeface.createFromAsset(getContext().getAssets(), "fonts/julius-sans-one" + ".ttf");
    for (int i = 0; i < pagerTitleStrip.getChildCount(); i++) {
      if ((pagerTitleStrip.getChildAt(i) instanceof TextView)) {
        final TextView titleView = ((TextView) pagerTitleStrip.getChildAt(i));
        titleView.setTypeface(localTypeface);
      }
    }
  }
}
