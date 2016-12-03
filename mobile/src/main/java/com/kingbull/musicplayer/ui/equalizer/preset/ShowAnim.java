package com.kingbull.musicplayer.ui.equalizer.preset;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public final class ShowAnim extends Animation {
  int targetHeight;
  View view;
  int currentHeight;

  public ShowAnim(View view, int targetHeight) {
    this.view = view;
    this.targetHeight = targetHeight;
    this.currentHeight = view.getHeight();
  }

  @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
    int newHeight = currentHeight + (int) ((targetHeight - currentHeight) * interpolatedTime);
    view.getLayoutParams().height = newHeight;
    view.requestLayout();
  }

  @Override public void initialize(int width, int height, int parentWidth, int parentHeight) {
    super.initialize(width, height, parentWidth, parentHeight);
  }

  @Override public boolean willChangeBounds() {
    return true;
  }
}