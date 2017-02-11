package com.kingbull.musicplayer.ui.base.animators;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import com.kingbull.musicplayer.R;

/**
 * CardView animation logic abstraction
 *
 * @author Jorge Castillo Pérez
 */
public interface SlideHorizontal {
  interface Listener {
    void onInAnimationFinished();

    void onOutAnimationFinished();

    Listener NONE = new Default();

    class Default implements SlideHorizontal.Listener {
      @Override public void onInAnimationFinished() {
      }

      @Override public void onOutAnimationFinished() {
      }
    }
  }

  interface Animator {
    void animateIn(View view, SlideHorizontal.Listener listener);

    void animateOut(View view, SlideHorizontal.Listener listener);
  }

  class Animation implements SlideHorizontal.Animator {
    private final int DURATION = 300;

    @Override public void animateIn(final View view, final SlideHorizontal.Listener listener) {
      android.view.animation.Animation animation1 =
          AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_in_right);
      animation1.setDuration(DURATION);
      animation1.setInterpolator(new OvershootInterpolator(1.5f));
      animation1.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
        @Override public void onAnimationStart(android.view.animation.Animation animation) {
          view.setVisibility(View.VISIBLE);
        }

        @Override public void onAnimationEnd(android.view.animation.Animation animation) {
          if (listener != null) listener.onInAnimationFinished();
        }

        @Override public void onAnimationRepeat(android.view.animation.Animation animation) {
        }
      });
      view.startAnimation(animation1);
    }

    @Override public void animateOut(View view, final SlideHorizontal.Listener listener) {
      android.view.animation.Animation animation =
          AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_out_left);
      animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
        @Override public void onAnimationStart(android.view.animation.Animation animation) {
        }

        @Override public void onAnimationEnd(android.view.animation.Animation animation) {
          if (listener != null) listener.onOutAnimationFinished();
        }

        @Override public void onAnimationRepeat(android.view.animation.Animation animation) {
        }
      });
      view.startAnimation(animation);
    }
  }
}
