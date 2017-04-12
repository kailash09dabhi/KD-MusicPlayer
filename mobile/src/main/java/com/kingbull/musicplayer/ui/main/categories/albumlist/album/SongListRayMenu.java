package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;

/**
 * @author Kailash Dabhi
 * @date 11/24/2016.
 */
public final class SongListRayMenu extends com.kingbull.musicplayer.ui.base.view.raymenu.RayMenu {
  private final static int ADD_TO_PLAYLIST = 0;
  private final static int SORT = 1;
  private final static int SHUFFLE = 2;
  private static final int[] MENUS = {
      ADD_TO_PLAYLIST, SORT, SHUFFLE
  };
  private OnMenuClickListener onMenuClickListener;

  public SongListRayMenu(Context context) {
    super(context);
    init();
  }

  private void init() {
    final int itemCount = MENUS.length;
    int fillColor = 0;
    for (int i = 0; i < itemCount; i++) {
      ImageView item = new ImageView(getContext());
      int dp4 = IconDrawable.dpToPx(1);
      item.setPadding(dp4, dp4, dp4, dp4);
      if (MENUS[i] == ADD_TO_PLAYLIST) {
        item.setImageDrawable(
            new IconDrawable(R.drawable.ic_playlist_add_48dp, Color.WHITE, fillColor));
      } else if (MENUS[i] == SORT) {
        item.setImageDrawable(new IconDrawable(R.drawable.ic_sort_48dp, Color.WHITE, fillColor));
      } else if (MENUS[i] == SHUFFLE) {
        item.setImageDrawable(new IconDrawable(R.drawable.ic_shuffle_48dp, Color.WHITE, fillColor));
      }
      final int position = i;
      addItem(item, new OnClickListener() {
        @Override public void onClick(View v) {
          postDelayed(new Runnable() {
            @Override public void run() {
              if (MENUS[position] == ADD_TO_PLAYLIST) {
                onMenuClickListener.onAddToPlaylistMenuClick();
              } else if (MENUS[position] == SORT) {
                onMenuClickListener.onSortMenuClick();
              } else if (MENUS[position] == SHUFFLE) {
                onMenuClickListener.onShuffleMenuClick();
              }
            }
          }, 400);
        }
      });// Add a menu item
    }
  }

  public SongListRayMenu(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
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
