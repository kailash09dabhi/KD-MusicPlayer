package com.kingbull.musicplayer.ui.music;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.player.PlayMode;

/**
 * @author Kailash Dabhi
 * @date 11/11/2016.
 */

public final class PlayModeToggleView extends ImageView {
  private PlayMode playMode;

  public PlayModeToggleView(Context context) {
    super(context);
  }

  public PlayModeToggleView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PlayModeToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void takePlayMode(PlayMode playMode) {
    this.playMode = playMode;
    if (playMode == null) {
      this.playMode = PlayMode.getDefault();
    }
    switch (playMode) {
      case LIST:
        setImageResource(R.drawable.ic_play_mode_list);
        break;
      case LOOP:
        setImageResource(R.drawable.ic_play_mode_loop);
        break;
      case SHUFFLE:
        setImageResource(R.drawable.ic_play_mode_shuffle);
        break;
      case SINGLE:
        setImageResource(R.drawable.ic_play_mode_single);
        break;
    }
  }
}
