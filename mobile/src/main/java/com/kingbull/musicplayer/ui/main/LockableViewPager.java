package com.kingbull.musicplayer.ui.main;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public final class LockableViewPager extends ViewPager {
  private boolean isPagingEnabled = true;

  public LockableViewPager(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    if (this.isPagingEnabled) {
      return super.onInterceptTouchEvent(paramMotionEvent);
    }
    return false;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (this.isPagingEnabled) {
      return super.onTouchEvent(paramMotionEvent);
    }
    return false;
  }

  public void setPagingEnabled(boolean paramBoolean) {
    this.isPagingEnabled = paramBoolean;
  }
}
