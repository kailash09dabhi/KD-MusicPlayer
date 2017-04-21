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

    public Animation() {
      this(500, Listener.NONE);
    }

    public Animation(int duration, Listener listener) {
      this.duration = duration;
      this.listener = listener;
    }

    public Animation(int duration) {
      this.duration = duration;
      this.listener = Listener.NONE;
    }

    @Override public void fadeIn(final View view) {
      view.setAlpha(0);
      view.animate().setDuration(duration).alpha(1).withStartAction(new Runnable() {
        @Override public void run() {
          view.setAlpha(0);
          view.setVisibility(View.VISIBLE);
        }
      }).withEndAction(new Runnable() {
        @Override public void run() {
          listener.onFadeInAnimationFinished();
        }
      }).start();
    }

    @Override public void fadeOut(final View view) {
      view.animate().setDuration(duration).alpha(0).withEndAction(new Runnable() {
        @Override public void run() {
          view.setVisibility(View.GONE);
          listener.onFadeOutAnimationFinished();
        }
      }).start();
    }
  }
}
