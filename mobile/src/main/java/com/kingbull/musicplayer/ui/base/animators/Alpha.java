package com.kingbull.musicplayer.ui.base.animators;

import android.view.View;

public interface Alpha {
  interface Listener {
    Alpha.Listener NONE = new Alpha.Listener.Default();

    void onInAnimationFinished();

    void onOutAnimationFinished();

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
    private final int duration;

    public Animation() {
      this(500);
    }

    public Animation(int duration) {
      this.duration = duration;
    }

    @Override public void animateIn(final View view, final Listener listener) {
      view.setAlpha(0);
      view.animate().setDuration(duration).alpha(1).withStartAction(new Runnable() {
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
      view.animate().setDuration(duration).alpha(0).withEndAction(new Runnable() {
        @Override public void run() {
          view.setVisibility(View.GONE);
          if (listener != null) listener.onOutAnimationFinished();
        }
      }).start();
    }
  }
}
