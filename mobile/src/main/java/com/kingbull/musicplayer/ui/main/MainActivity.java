package com.kingbull.musicplayer.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.main.categories.artistlist.artist.Artist;

public final class MainActivity extends BaseActivity<Artist.Presenter> {
  @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
  @BindView(R.id.viewPager) LockableViewPager lockableViewPager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    lockableViewPager.setPagingEnabled(false);
    lockableViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
    lockableViewPager.setOffscreenPageLimit(4);
    lockableViewPager.setCurrentItem(0);
    bottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_all_songs:
                lockableViewPager.setCurrentItem(0);
                break;
              case R.id.action_categories:
                lockableViewPager.setCurrentItem(1);
                break;
              case R.id.action_playlists:
                lockableViewPager.setCurrentItem(2);
                break;
              case R.id.action_my_files:
                lockableViewPager.setCurrentItem(3);
                break;
              case R.id.action_settings:
                lockableViewPager.setCurrentItem(4);
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