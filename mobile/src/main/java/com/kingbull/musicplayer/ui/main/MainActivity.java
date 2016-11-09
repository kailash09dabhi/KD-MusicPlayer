package com.kingbull.musicplayer.ui.main;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import java.util.Random;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public final class MainActivity extends AppCompatActivity {

  PagerAdapter pagerAdapter;
  @BindView(R.id.pager) com.kingbull.musicplayer.ui.main.ViewPagerParallax viewPager;
  @BindView(R.id.tabTitleStrip) PagerTitleStrip pagerTitleStrip;
  @BindArray(R.array.tabs) String[] tabs;
  int arrayBg[] = {
      R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6,
      R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
      R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17,
      R.drawable.a18,
  };   @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this, tabs);
    viewPager.setAdapter(pagerAdapter);
    setupPagerTitleStrip();
    int i2 = new Random().nextInt(18);
    viewPager.setBackgroundAsset(arrayBg[i2]);
    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    viewPager.setOffscreenPageLimit(3);
    viewPager.setCurrentItem(2);
  }

  private void setupPagerTitleStrip() {
    Typeface localTypeface = Typeface.createFromAsset(getAssets(), "fonts/julius-sans-one.ttf");
    for (int i1 = 0; i1 < pagerTitleStrip.getChildCount(); i1++) {
      if ((pagerTitleStrip.getChildAt(i1) instanceof TextView)) {
        ((TextView) pagerTitleStrip.getChildAt(i1)).setTypeface(localTypeface);
      }
    }
  }
}