package com.kingbull.musicplayer.ui.base.animators;

import android.view.View;

public interface Alpha {
  interface Listener {
    Alpha.Listener NONE = new Alpha.Listener.Default();

    void onFadeInAnimationFinished();

    void onFadeOutAnimationFinished();

    class Default implements Alpha.Listener {
      @Override public void onFadeInAnimationFinished() {
      }

      @Override public void onFadeOutAnimationFinished() {
      }
    }
  }

  interface Animator {
    void fadeIn(View view);

    void fadeOut(View view);
  }

  class Animation implements Alpha.Animator {
    private final int duration;
    private final Listener listener;
    private final float initialAlpha;

    public Animation() {
      this(0, 500, Listener.NONE);
    }

    public Animation(float initialAlpha, int duration, Listener listener) {
      this.initialAlpha = initialAlpha;
      this.duration = duration;
      this.listener = listener;
    }

    public Animation(int duration, Listener listener) {
      this(0, duration, listener);
    }

    public Animation(float initialAlpha, int duration) {
      this(initialAlpha, duration, Listener.NONE);
    }

    public Animation(int duration) {
      this(0, duration, Listener.NONE);
    }

    @Override public void fadeIn(final View view) {
      view.setAlpha(initialAlpha);
      view.animate().setDuration(duration).alpha(1).withStartAction(new Runnable() {
        @Override public void run() {
          view.setAlpha(initialAlpha);
          view.setVisibility(View.VISIBLE);
        }
      }).withEndAction(new Runnable() {
        @Override public void run() {
          listener.onFadeInAnimationFinished();
        }
      }).start();
    }

    @Override public void fadeOut(final View view) {
      view.animate().setDuration(duration).alpha(initialAlpha).withEndAction(new Runnable() {
        @Override public void run() {
          view.setVisibility(View.GONE);
          listener.onFadeOutAnimationFinished();
        }
      }).start();
    }
  }
}
