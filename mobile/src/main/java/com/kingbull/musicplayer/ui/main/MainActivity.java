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
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.UiColors;
import com.kingbull.musicplayer.ui.main.categories.artistlist.artist.Artist;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.Random;

public final class MainActivity extends BaseActivity<Artist.Presenter> {
  private final int arrayBg[] = {
      R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6,
      R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
      R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17, R.drawable.a18
  };
  @BindView(R.id.viewPager) ViewPagerParallax viewPager;
  @BindArray(R.array.main_tabs) String[] tabs;
  @BindView(R.id.sliding_tabs) TabLayout tabLayout;
  private MainPagerAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    adapter = new MainPagerAdapter(getSupportFragmentManager(), this, tabs);
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(4);
    viewPager.setCurrentItem(0);
    viewPager.setBackgroundAsset(arrayBg[new Random().nextInt(18)], getWindow());
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
    //io.reactivex.Observable.interval(2, 10, TimeUnit.SECONDS)
    //    .observeOn(AndroidSchedulers.mainThread())
    //    .doOnNext(new Consumer<Long>() {
    //      int i = 18;
    //
    //      @Override public void accept(Long aLong) throws Exception {
    //        viewPager.setBackgroundAsset(arrayBg[i++], getWindow());
    //        if (i >= arrayBg.length) i = 18;
    //      }
    //    })
    //    .subscribe();
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
              Typeface.createFromAsset(getAssets(), getString(R.string.font_julius_sans_one)),
              Typeface.BOLD);
        }
      }
    }
  }

  @Override protected void onPresenterPrepared(Artist.Presenter presenter) {
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.Artist();
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof PaletteEvent || o instanceof ThemeEvent) {
              int color = new UiColors().tab().intValue();
              tabLayout.setBackgroundColor(color);
              new ViewPagerEdgeEffectHack(viewPager).applyColor(color);
            }
          }
        });
  }
}