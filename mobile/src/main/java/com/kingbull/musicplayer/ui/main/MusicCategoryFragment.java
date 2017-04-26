package com.kingbull.musicplayer.ui.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof PaletteEvent || o instanceof ThemeEvent) {
              pagerTitleStrip.setBackgroundColor(smartTheme.header().intValue());
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
    Typeface typeface =
        Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_title));
    int childCount = pagerTitleStrip.getChildCount();
    for (int i = 0; i < childCount; i++) {
      if ((pagerTitleStrip.getChildAt(i) instanceof TextView)) {
        final TextView titleView = ((TextView) pagerTitleStrip.getChildAt(i));
        titleView.setTypeface(typeface);
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen.pager_title_text_size));
      }
    }
  }
}
