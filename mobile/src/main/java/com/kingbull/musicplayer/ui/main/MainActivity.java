package com.kingbull.musicplayer.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.PiracyGuard;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.SmartPiracyGuard;
import com.kingbull.musicplayer.domain.storage.preferences.Background;
import com.kingbull.musicplayer.event.BackgroundEvent;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.event.TransparencyChangedEvent;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.DeviceConfig;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.RequiredPermissions;
import com.kingbull.musicplayer.ui.base.analytics.FbKeyHash;
import com.kingbull.musicplayer.ui.main.categories.artistlist.artist.Artist;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import com.kingbull.musicplayer.ui.splash.SplashActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import javax.inject.Inject;

public final class MainActivity extends BaseActivity<Artist.Presenter> {
  private final Pictures pictures = new Pictures();
  @BindView(R.id.viewPager) ViewPagerParallax viewPager;
  @BindArray(R.array.main_tabs) String[] tabs;
  @BindView(R.id.sliding_tabs) TabLayout tabLayout;
  @Inject Player player;
  @Inject SharedPreferences sharedPreferences;
  private PiracyGuard piracyGuard;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!new RequiredPermissions(this).isGranted()) {
      finish();
      startActivity(new Intent(this, SplashActivity.class));
    }
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    MusicPlayerApp.instance().component().inject(this);
    new DeviceConfig(getResources()).writeToLogcat();
    new FbKeyHash().print(this);
    piracyGuard = new SmartPiracyGuard(this);
    piracyGuard.check();
    if (player.isPlaying()) {
      startActivity(new Intent(this, MusicPlayerActivity.class));
    }
    MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), tabs);
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(4);
    viewPager.setCurrentItem(0);
    viewPager.setBackgroundAsset(
        new Background.Smart(sharedPreferences).resId(), getWindow()
    );
    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override public void onPageSelected(int position) {
      }

      @Override public void onPageScrollStateChanged(int state) {
      }
    });
    tabLayout.setupWithViewPager(viewPager);
    setupTabLayout();
    getSupportFragmentManager().addOnBackStackChangedListener(
            () -> {
              // Update your UI here.
              if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                Log.e("backstack", getSupportFragmentManager().getBackStackEntryAt(
                    getSupportFragmentManager().getBackStackEntryCount() - 1).getName());
              }
            });
  }

  @Override protected void onDestroy() {
    piracyGuard.free();
    super.onDestroy();
  }

  @NonNull @Override protected PresenterFactory<Artist.Presenter> presenterFactory() {
    return new PresenterFactory.Artist();
  }

  @Override protected void onPresenterPrepared(Artist.Presenter presenter) {
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> {
          if ((o instanceof PaletteEvent) || (o instanceof ThemeEvent)
              || (o instanceof TransparencyChangedEvent)) {
            int color = smartTheme.tab().intValue();
            tabLayout.setBackgroundColor(color);
            new ViewPagerEdgeEffectHack(viewPager).applyColor(color);
            viewPager.applyBackgroundAccordingToTheme();
          } else if (o instanceof BackgroundEvent) {
            viewPager.setBackgroundAsset(
                new Background.Smart(sharedPreferences).resId(), getWindow()
            );
          }
        });
  }

  private void setupTabLayout() {
    ViewGroup viewGroup = (ViewGroup) tabLayout.getChildAt(0);
    int tabsCount = viewGroup.getChildCount();
    Typeface typeface = Typeface.createFromAsset(getAssets(), getString(R.string.font_title));
    for (int j = 0; j < tabsCount; j++) {
      ViewGroup tabViewGroup = (ViewGroup) viewGroup.getChildAt(j);
      int tabChildsCount = tabViewGroup.getChildCount();
      for (int i = 0; i < tabChildsCount; i++) {
        if ((tabViewGroup.getChildAt(i) instanceof TextView)) {
          final TextView titleView = ((TextView) tabViewGroup.getChildAt(i));
          titleView.setTypeface(typeface);
        }
      }
    }
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (intent.getExtras() != null) {
      if (intent.getStringExtra("from").equals("notification")) {
        if (player.isPlaying()) {
          startActivity(new Intent(this, MusicPlayerActivity.class));
        }
      }
    }
  }
}