package com.kingbull.musicplayer.ui.music;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.kingbull.musicplayer.domain.Music;

/**
 * @author Kailash Dabhi
 * @date 11/11/2016.
 */
public final class MusicSeekBar extends AppCompatSeekBar {
  private final Handler handler = new Handler();
  private MusicPlayer.Presenter presenter;
  private Music song;
  private Runnable progressRunnable = new Runnable() {
    @Override public void run() {
      presenter.onSeekbarProgress();
    }
  };

  public MusicSeekBar(Context context) {
    super(context);
  }

  public MusicSeekBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MusicSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  void takePresenter(MusicPlayer.Presenter presenter) {
    this.presenter = presenter;
  }

  void updateMusic(Music song) {
    this.song = song;
    setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
          presenter.onProgressChanged(
              (int) (MusicSeekBar.this.song.media().duration() * ((float) progress / 100)));
        }
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
        dontAnimate();
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
        presenter.onStopTrackingTouch(seekBar.getProgress());
      }
    });
  }

  public void dontAnimate() {
    handler.removeCallbacks(progressRunnable);
  }

  @Override public Parcelable onSaveInstanceState() {
    Bundle bundle = new Bundle();
    bundle.putParcelable("song", (Parcelable) song);
    bundle.putParcelable("instanceState", super.onSaveInstanceState());
    return bundle;
  }

  @Override public void onRestoreInstanceState(Parcelable state) {
    if (state instanceof Bundle) {
      Bundle bundle = (Bundle) state;
      this.song = bundle.getParcelable("song");
      // ... load everything
      state = bundle.getParcelable("instanceState");
    }
    super.onRestoreInstanceState(state);
  }

  public void updateThumb(int progress) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      setProgress(progress, true);
    } else {
      setProgress(progress);
    }
  }

  public void startProgresssAnimation() {
    handler.post(progressRunnable);
  }

  public void animateProgressAfter(long updateProgressInterval) {
    handler.postDelayed(progressRunnable, updateProgressInterval);
  }
}
