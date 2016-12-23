package com.kingbull.musicplayer.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.main.categories.artistlist.artist.Artist;
import java.util.Random;

public final class MainActivity extends BaseActivity<Artist.Presenter> {
  @BindView(R.id.bottom_navigation) RichBottomNavigationView bottomNavigationView;
  @BindView(R.id.viewPager) ViewPagerParallax viewPager;
  private final int arrayBg[] = {
      R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6,
      R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
      R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17,
      R.drawable.a18,
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    final MainLayout mainLayout = (MainLayout) bottomNavigationView.getParent();
    viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
    viewPager.setOffscreenPageLimit(4);
    viewPager.setCurrentItem(0);
    viewPager.setBackgroundAsset(arrayBg[new Random().nextInt(18)], getWindow());
    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mainLayout.drawParallax(position, positionOffset);
      }

      @Override public void onPageSelected(int position) {
        bottomNavigationView.setSelectedItem(position);
      }

      @Override public void onPageScrollStateChanged(int state) {
      }
    });
    bottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_all_songs:
                if (viewPager.getCurrentItem() != 0) viewPager.setCurrentItem(0);
                break;
              case R.id.action_categories:
                if (viewPager.getCurrentItem() != 1) viewPager.setCurrentItem(1);
                break;
              case R.id.action_playlists:
                if (viewPager.getCurrentItem() != 2) viewPager.setCurrentItem(2);
                break;
              case R.id.action_my_files:
                if (viewPager.getCurrentItem() != 3) viewPager.setCurrentItem(3);
                break;
              case R.id.action_settings:
                if (viewPager.getCurrentItem() != 4) viewPager.setCurrentItem(4);
                break;
            }
            return false;
          }
        });
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

  @Override protected void onPresenterPrepared(Artist.Presenter presenter) {
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.Artist();
  }
}