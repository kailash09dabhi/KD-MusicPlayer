package com.kingbull.musicplayer.ui.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.main.categories.playlists.members.Members;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author Kailash Dabhi
 * @date 12/20/2016.
 */
public final class MusicCategoryFragment extends BaseFragment<Members.Presenter> {
  @BindView(R.id.pager) ViewPager viewPager;
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
    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    viewPager.setOffscreenPageLimit(3);
    viewPager.setCurrentItem(2);
    pagerTitleStrip.setBackgroundColor(smartColorTheme.screen().intValue());
    setupPagerTitleStrip();
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof PaletteEvent || o instanceof ThemeEvent) {
              pagerTitleStrip.setBackgroundColor(smartColorTheme.header().intValue());
            }
          }
        });
  }

  @Override protected PresenterFactory<Members.Presenter> presenterFactory() {
    return new PresenterFactory.MusicListOfPlaylist();
  }

  @Override protected void onPresenterPrepared(Members.Presenter presenter) {
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
