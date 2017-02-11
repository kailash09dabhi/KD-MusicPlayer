package com.kingbull.musicplayer.ui.base.animators;

import android.view.View;

/**
 * CardView animation logic abstraction
 *
 * @author Jorge Castillo Pérez
 */
public interface Alpha {
  interface Listener {
    void onInAnimationFinished();

    void onOutAnimationFinished();

    Alpha.Listener NONE = new Alpha.Listener.Default();

    class Default implements Alpha.Listener {
      @Override public void onInAnimationFinished() {
      }

      @Override public void onOutAnimationFinished() {
      }
    }
  }

  interface Animator {

    void animateIn(View view, Listener listener);

    void animateOut(View view, Listener listener);
  }

  class Animation implements Alpha.Animator {
    private final int DURATION = 700;

    @Override public void animateIn(final View view, final Listener listener) {
      view.setAlpha(0);
      view.animate().setDuration(DURATION).alpha(1).withStartAction(new Runnable() {
        @Override public void run() {
          view.setAlpha(0);
          view.setVisibility(View.VISIBLE);
        }
      }).withEndAction(new Runnable() {
        @Override public void run() {
          if (listener != null) listener.onInAnimationFinished();
        }
      }).start();
    }

    @Override public void animateOut(final View view, final Listener listener) {
      view.animate().setDuration(DURATION).alpha(0).withEndAction(new Runnable() {
        @Override public void run() {
          view.setVisibility(View.GONE);
          if (listener != null) listener.onOutAnimationFinished();
        }
      }).start();
    }
  }
}
