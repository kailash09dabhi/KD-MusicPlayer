package com.kingbull.musicplayer.ui.base.animators;

import android.view.View;

public interface Alpha {
  interface FadeOutListener {
    Alpha.FadeOutListener NONE = () -> {
    };

    void onFadeOutAnimationFinished();
  }

  interface FadeInListener {
    Alpha.FadeInListener NONE = () -> {
    };

    void onFadeInAnimationFinished();
  }

  interface Animator {
    void fadeIn(View view, FadeInListener listener);

    void fadeIn(View view);

    void fadeOut(View view, FadeOutListener listener);

    void fadeOut(View view);
  }

  class Animation implements Alpha.Animator {
    private final int duration;
    private final float initialAlpha;

    public Animation() {
      this(0, 500);
    }

    public Animation(float initialAlpha, int duration) {
      this.initialAlpha = initialAlpha;
      this.duration = duration;
    }

    public Animation(int duration) {
      this(0, duration);
    }

    @Override public void fadeIn(final View view, final FadeInListener listener) {
      view.setAlpha(initialAlpha);
      view.animate().setDuration(duration).alpha(1).withStartAction(() -> {
        view.setAlpha(initialAlpha);
        view.setVisibility(View.VISIBLE);
      }).withEndAction(() -> listener.onFadeInAnimationFinished()).start();
    }

    @Override public void fadeIn(View view) {
      fadeIn(view, FadeInListener.NONE);
    }

    @Override public void fadeOut(View view) {
      fadeOut(view, FadeOutListener.NONE);
    }

    @Override public void fadeOut(final View view, final FadeOutListener listener) {
      view.animate().setDuration(duration).alpha(initialAlpha).withEndAction(() -> {
        view.setVisibility(View.GONE);
        listener.onFadeOutAnimationFinished();
      }).start();
    }
  }
}
