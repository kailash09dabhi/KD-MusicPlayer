package com.kingbull.musicplayer.ui.coverarts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.kingbull.musicplayer.R;

/**
 * Created by Kailash Dabhi on 11-07-2016.
 * You can contact us at kailash09dabhi@gmail.com OR on skype(kailash.09)
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
 */
public final class ProgressOverlayLayout extends FrameLayout {
  public ProgressOverlayLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    inflate(getContext(), R.layout.progress_overlay, this);
  }

  public void showProgress() {
    animateView(View.VISIBLE, 0.9f, 0);
  }

  public void hideProgress() {
    animateView(View.GONE, 0, 200);
  }

  /**
   * @param toVisibility Visibility at the end of animation
   * @param toAlpha Alpha at the end of animation
   * @param duration Animation duration in ms
   */
  private void animateView(final int toVisibility, float toAlpha, int duration) {
    boolean show = toVisibility == View.VISIBLE;
    if (show) {
      setAlpha(0);
    }
    setVisibility(View.VISIBLE);
    animate().setDuration(duration)
        .alpha(show ? toAlpha : 0)
        .setListener(new AnimatorListenerAdapter() {
          @Override public void onAnimationEnd(Animator animation) {
            setVisibility(toVisibility);
          }
        });
  }
}
