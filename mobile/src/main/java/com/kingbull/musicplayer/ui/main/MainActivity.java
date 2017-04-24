package com.kingbull.musicplayer.ui.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.main.categories.artistlist.artist.Artist;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public final class MainActivity extends BaseActivity<Artist.Presenter> {
  private final int arrayBg[] = {
      R.drawable.k1, R.drawable.k2, R.drawable.k3, R.drawable.k4, R.drawable.k5, R.drawable.k6,
      R.drawable.k7, R.drawable.k8, R.drawable.k9, R.drawable.k10, R.drawable.k11, R.drawable.k12,
      R.drawable.k13, R.drawable.k14, R.drawable.k15, R.drawable.k16, R.drawable.k17,
      R.drawable.k18,
  };
  @BindView(R.id.viewPager) ViewPagerParallax viewPager;
  @BindArray(R.array.main_tabs) String[] tabs;
  @BindView(R.id.sliding_tabs) TabLayout tabLayout;
  Disposable disposable;
  private MainPagerAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    MusicPlayerApp.instance().component().inject(this);
    adapter = new MainPagerAdapter(getSupportFragmentManager(), tabs);
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(4);
    viewPager.setCurrentItem(0);
    viewPager.setBackgroundAsset(arrayBg[new Random().nextInt(12)], getWindow());
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
        new FragmentManager.OnBackStackChangedListener() {
          public void onBackStackChanged() {
            // Update your UI here.
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
              Log.e("backstack", getSupportFragmentManager().getBackStackEntryAt(
                  getSupportFragmentManager().getBackStackEntryCount() - 1).getName());
            }
          }
        });
    disposable = io.reactivex.Observable.interval(2, 20, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(new Consumer<Long>() {
          int i = 0;

          @Override public void accept(Long aLong) throws Exception {
            viewPager.setBackgroundAsset(arrayBg[i++], getWindow());
            if (i >= arrayBg.length) i = 0;
          }
        })
        .subscribe();
  }

  @Override protected void onDestroy() {
    disposable.dispose();
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
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof PaletteEvent || o instanceof ThemeEvent) {
              int color = smartTheme.tab().intValue();
              tabLayout.setBackgroundColor(color);
              new ViewPagerEdgeEffectHack(viewPager).applyColor(color);
              viewPager.applyBackgroundAccordingToTheme();
            }
          }
        });
  }

  private void setupTabLayout() {
    ViewGroup viewGroup = (ViewGroup) tabLayout.getChildAt(0);
    int tabsCount = viewGroup.getChildCount();
    for (int j = 0; j < tabsCount; j++) {
      ViewGroup tabViewGroup = (ViewGroup) viewGroup.getChildAt(j);
      int tabChildsCount = tabViewGroup.getChildCount();
      for (int i = 0; i < tabChildsCount; i++) {
        View tabViewChild = tabViewGroup.getChildAt(i);
        if (tabViewChild instanceof TextView) {
          ((TextView) tabViewChild).setTypeface(
              Typeface.createFromAsset(getAssets(), getString(R.string.font_title)), Typeface.BOLD);
        }
      }
    }
  }
}