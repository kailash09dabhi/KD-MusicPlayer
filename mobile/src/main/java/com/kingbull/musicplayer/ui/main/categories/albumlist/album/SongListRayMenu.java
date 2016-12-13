package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.kingbull.musicplayer.R;

/**
 * @author Kailash Dabhi
 * @date 11/24/2016.
 */

public final class SongListRayMenu extends com.kingbull.musicplayer.ui.raymenu.RayMenu {
  private static final int[] MENUS = {
      R.drawable.composer_button_queue, R.drawable.composer_button_sort,
      R.drawable.composer_button_shuffle
  };
  private OnMenuClickListener onMenuClickListener;

  public SongListRayMenu(Context context) {
    super(context);
    init();
  }

  public SongListRayMenu(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    final int itemCount = MENUS.length;
    for (int i = 0; i < itemCount; i++) {
      ImageView item = new ImageView(getContext());
      item.setImageResource(MENUS[i]);
      final int position = i;
      addItem(item, new OnClickListener() {

        @Override public void onClick(View v) {
          postDelayed(new Runnable() {
            @Override public void run() {
              if (MENUS[position] == R.drawable.composer_button_queue) {
                onMenuClickListener.onAddToPlaylistMenuClick();
              } else if (MENUS[position] == R.drawable.composer_button_sort) {
                onMenuClickListener.onSortMenuClick();
              } else if (MENUS[position] == R.drawable.composer_button_shuffle) {
                onMenuClickListener.onShuffleMenuClick();
              }
            }
          }, 400);
        }
      });// Add a menu item
    }
  }

  public void addOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
    this.onMenuClickListener = onMenuClickListener;
  }

  public interface OnMenuClickListener {

    void onShuffleMenuClick();

    void onAddToPlaylistMenuClick();

    void onSortMenuClick();
  }
}
