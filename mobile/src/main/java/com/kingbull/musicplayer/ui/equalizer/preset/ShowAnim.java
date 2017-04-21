package com.kingbull.musicplayer.ui.equalizer.preset;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

final class ShowAnim extends Animation {
  private int targetHeight;
  private View view;
  private int currentHeight;

  public ShowAnim(View view, int targetHeight) {
    this.view = view;
    this.targetHeight = targetHeight;
    this.currentHeight = view.getHeight();
  }

  @Override public void initialize(int width, int height, int parentWidth, int parentHeight) {
    super.initialize(width, height, parentWidth, parentHeight);
  }

  @Override public boolean willChangeBounds() {
    return true;
  }

  @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
    int newHeight = currentHeight + (int) ((targetHeight - currentHeight) * interpolatedTime);
    view.getLayoutParams().height = newHeight;
    view.requestLayout();
  }
}