package com.kingbull.musicplayer.ui.main.categories.all;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.main.categories.all.raymenu.RayMenu;

/**
 * @author Kailash Dabhi
 * @date 11/24/2016.
 */

public final class SongMenu extends RayMenu {
  private static final int[] MENUS = {
      R.drawable.composer_button_queue, R.drawable.composer_button_sort,
      R.drawable.composer_button_shuffle, R.drawable.composer_icn_search,
      R.drawable.composer_icn_settings,
  };
  private OnMenuClickListener onMenuClickListener;

  public SongMenu(Context context) {
    super(context);
    init();
  }

  public SongMenu(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    final int itemCount = MENUS.length;
    for (int i = 0; i < itemCount; i++) {
      ImageView item = new ImageView(getContext());
      item.setImageResource(MENUS[i]);
      final int position = i;
      addItem(item, new View.OnClickListener() {

        @Override public void onClick(View v) {
          postDelayed(new Runnable() {
            @Override public void run() {
              if (MENUS[position] == R.drawable.composer_icn_search) {
                onMenuClickListener.onSearchMenuClick();
              } else if (MENUS[position] == R.drawable.composer_icn_settings) {
                onMenuClickListener.onSettingsMenuClick();
              } else if (MENUS[position] == R.drawable.composer_button_shuffle) {
                onMenuClickListener.onShuffleMenuClick();
              } else if (MENUS[position] == R.drawable.composer_button_queue) {
                onMenuClickListener.onAddToPlaylistMenuClick();
              } else if (MENUS[position] == R.drawable.composer_button_sort) {
                onMenuClickListener.onSortMenuClick();
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

  interface OnMenuClickListener {
    void onSearchMenuClick();

    void onShuffleMenuClick();

    void onSettingsMenuClick();

    void onAddToPlaylistMenuClick();

    void onSortMenuClick();
  }
}
