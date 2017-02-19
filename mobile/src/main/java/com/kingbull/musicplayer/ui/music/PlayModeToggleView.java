package com.kingbull.musicplayer.ui.music;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.player.MusicMode;

/**
 * @author Kailash Dabhi
 * @date 11/11/2016.
 */

public final class PlayModeToggleView extends ImageView {

  public PlayModeToggleView(Context context) {
    super(context);
  }

  public PlayModeToggleView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PlayModeToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void takePlayMode(MusicMode musicMode) {
    switch (musicMode) {
      case REPEAT_ALL:
        setImageResource(R.drawable.ic_play_mode_loop);
        break;
      case REPEAT_SINGLE:
        setImageResource(R.drawable.ic_play_mode_single);
        break;
      case REPEAT_NONE:
        setImageResource(R.drawable.ic_play_mode_list);
        break;
    }
  }
}
