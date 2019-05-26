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

    void onAnimationFinished();

    class Default implements SlideHorizontal.Listener {
      @Override public void onAnimationFinished() {
      }
    }
  }

  interface Animator {
    void slide(View view, int translationX);
  }

  class Animation implements SlideHorizontal.Animator {
    private final int duration;
    private final Listener listener;

    public Animation() {
      this(500, Listener.NONE);
    }

    public Animation(int duration, Listener listener) {
      this.duration = duration;
      this.listener = listener;
    }

    public Animation(int duration) {
      this(duration, Listener.NONE);
    }

    @Override public void slide(final View view, int translationX) {
      view.animate().withStartAction(() -> view.setVisibility(View.VISIBLE)).setDuration(duration).translationX(translationX).withEndAction(() -> {
        if (listener != null) listener.onAnimationFinished();
      }).start();
    }
  }
}
