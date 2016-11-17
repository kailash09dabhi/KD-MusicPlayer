package com.kingbull.musicplayer.ui.main;

import android.content.Context;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

public class PagerClickTitleStrip extends PagerTitleStrip {
  private TextView textPrev, textNext;
  private ViewPager pager;

  public PagerClickTitleStrip(Context arg0, AttributeSet arg1) {
    super(arg0, arg1);
    textPrev = (TextView) getChildAt(0);// This is the previous textview
    textNext = (TextView) getChildAt(2);// This is the next textview
    textPrev.setOnClickListener(new OnClickListener() {

      @Override public void onClick(View v) {
        if (pager != null && pager.getCurrentItem() != 0) {
          pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
      }
    });
    textNext.setOnClickListener(new OnClickListener() {

      @Override public void onClick(View v) {
        if (pager != null && pager.getCurrentItem() != pager.getAdapter().getCount() - 1) {
          pager.setCurrentItem(pager.getCurrentItem() + 1);
        }
      }
    });
    invalidate();
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    ViewParent parent = getParent();
    if (!(parent instanceof ViewPager)) {
      throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
    }
    pager = (ViewPager) parent;
  }
}
