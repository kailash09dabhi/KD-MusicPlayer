package com.kingbull.musicplayer.ui.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerTitleStrip;
import androidx.viewpager.widget.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.event.TransparencyChangedEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.Mvp.Presenter;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author Kailash Dabhi
 * @date 12/20/2016.
 */
public final class MusicCategoryFragment extends BaseFragment<Presenter> {
  @BindView(R.id.pager) ViewPager viewPager;
  @BindView(R.id.tabTitleStrip) PagerTitleStrip pagerTitleStrip;
  @BindArray(R.array.tabs) String[] tabs;
  private MusicCategoryPagerAdapter musicCategoryPagerAdapter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_music_category, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    musicCategoryPagerAdapter = new MusicCategoryPagerAdapter(getChildFragmentManager(), tabs);
    viewPager.setAdapter(musicCategoryPagerAdapter);
    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    viewPager.setOffscreenPageLimit(3);
    viewPager.setCurrentItem(1);
    pagerTitleStrip.setBackgroundColor(smartTheme.screen().intValue());
    setupPagerTitleStrip();
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> {
          // TODO: 7/31/2017 Is presenter and this subscribe should be here? something wrong i feel!
          if (presenter != null && presenter.hasView()) {
            if (o instanceof PaletteEvent || o instanceof ThemeEvent
                || o instanceof TransparencyChangedEvent) {
              pagerTitleStrip.setBackgroundColor(smartTheme.header().intValue());
            }
          } else {
            FirebaseCrashlytics.getInstance().recordException(
                new NullPointerException(
                    String.format(
                        "class: %s presenter- %s hasView- %b",
                        MusicCategoryFragment.class.getSimpleName(),
                        presenter, presenter != null && presenter.hasView()
                    )
                )
            );
          }
        });
  }

  @Override protected PresenterFactory presenterFactory() {
    return PresenterFactory.BASIC;
  }

  @Override protected void onPresenterPrepared(Presenter presenter) {
    presenter.takeView(this);
  }

  private void setupPagerTitleStrip() {
    Typeface typeface =
        Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_title));
    int childCount = pagerTitleStrip.getChildCount();
    for (int i = 0; i < childCount; i++) {
      if ((pagerTitleStrip.getChildAt(i) instanceof TextView)) {
        final TextView titleView = ((TextView) pagerTitleStrip.getChildAt(i));
        titleView.setTypeface(typeface);
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimensionPixelSize(R.dimen.pager_title_text_size));
      }
    }
  }
}
