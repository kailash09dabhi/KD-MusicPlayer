package com.kingbull.musicplayer.ui.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTitleStrip;
import android.util.Log;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.Presenter;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import java.util.Random;

public final class MainActivity extends BaseActivity {

  PagerAdapter pagerAdapter;
  @BindView(R.id.pager) com.kingbull.musicplayer.ui.main.ViewPagerParallax viewPager;
  @BindView(R.id.tabTitleStrip) PagerTitleStrip pagerTitleStrip;
  @BindArray(R.array.tabs) String[] tabs;
  int arrayBg[] = {
      R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6,
      R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
      R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17,
      R.drawable.a18,
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this, tabs);
    viewPager.setAdapter(pagerAdapter);
    int i2 = new Random().nextInt(18);
    viewPager.setBackgroundAsset(arrayBg[i2], getWindow());
    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    viewPager.setOffscreenPageLimit(3);
    viewPager.setCurrentItem(2);
    setupPagerTitleStrip();
    getSupportFragmentManager().addOnBackStackChangedListener(
        new FragmentManager.OnBackStackChangedListener() {
          public void onBackStackChanged() {
            // Update your UI here.
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
              Log.e("backstack", getSupportFragmentManager().getBackStackEntryAt(
                  getSupportFragmentManager().getBackStackEntryCount() - 1).getName());
            }
          }
        });
  }

  @Override protected void onPresenterPrepared(Presenter presenter) {
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.MyFiles();
  }

  private void setupPagerTitleStrip() {
    Typeface localTypeface = Typeface.createFromAsset(getAssets(), "fonts/julius-sans-one.ttf");
    for (int i = 0; i < pagerTitleStrip.getChildCount(); i++) {
      if ((pagerTitleStrip.getChildAt(i) instanceof TextView)) {
        final TextView titleView = ((TextView) pagerTitleStrip.getChildAt(i));
        titleView.setTypeface(localTypeface);
      }
    }
  }
}