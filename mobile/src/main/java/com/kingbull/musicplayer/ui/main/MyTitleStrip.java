package com.kingbull.musicplayer.ui.main;

import android.content.Context;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.TextView;

public class MyTitleStrip extends PagerTitleStrip {
  private TextView textPrev, textNext;
  private ViewPager pager;

  public MyTitleStrip(Context arg0, AttributeSet arg1) {
    super(arg0, arg1);
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
