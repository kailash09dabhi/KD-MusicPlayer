package com.kingbull.musicplayer.ui.base.animators;

import android.view.View;

/**
 * CardView animation logic abstraction
 *
 * @author Jorge Castillo Pérez
 */
public interface SlideHorizontal {
  interface Listener {
    Listener NONE = new Default();

    void onInAnimationFinished();

    void onOutAnimationFinished();

    class Default implements SlideHorizontal.Listener {
      @Override public void onInAnimationFinished() {
      }

      @Override public void onOutAnimationFinished() {
      }
    }
  }

  interface Animator {
    void animateIn(View view);

    void animateOut(View view);
  }

  class Animation implements SlideHorizontal.Animator {
    private final int DURATION = 700;
    private final Listener listener;

    public Animation() {
      this.listener = Listener.NONE;
    }

    @Override public void animateIn(final View view) {
      view.animate().withStartAction(new Runnable() {
        @Override public void run() {
          view.setVisibility(View.VISIBLE);
        }
      }).setDuration(DURATION).translationX(0).withEndAction(new Runnable() {
        @Override public void run() {
          if (listener != null) listener.onInAnimationFinished();
        }
      }).start();
    }

    @Override public void animateOut(final View view) {
      view.animate().withStartAction(new Runnable() {
        @Override public void run() {
          view.setVisibility(View.VISIBLE);
        }
      }).setDuration(DURATION).translationX(view.getWidth()).withEndAction(new Runnable() {
        @Override public void run() {
          if (listener != null) listener.onOutAnimationFinished();
        }
      }).start();
    }
  }
}
