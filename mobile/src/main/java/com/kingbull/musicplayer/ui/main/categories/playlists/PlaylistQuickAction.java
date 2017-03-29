package com.kingbull.musicplayer.ui.main.categories.playlists;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.ActionItem;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.QuickAction;

/**
 * @author Kailash Dabhi
 * @date 12/8/2016.
 */
public final class PlaylistQuickAction {
  private final int ID_RENAME = 1;
  private final int ID_DELETE = 5;
  private Activity activity;
  private QuickAction quickAction;
  private PlaylistQuickActionListener playlistQuickActionListener;

  public PlaylistQuickAction(final Activity activity) {
    this.activity = activity;
    final ActionItem renameItem = new ActionItem(ID_RENAME, "Rename",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_play));
    final ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_delete));
    //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
    quickAction = new QuickAction(activity);
    //setup the action item click listener
    quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
      @Override public void onItemClick(QuickAction quickAction, int pos, int actionId) {
        ActionItem actionItem = quickAction.getActionItem(pos);
        if (actionId == ID_RENAME) {
          playlistQuickActionListener.rename();
        } else if (actionId == ID_DELETE) {
          playlistQuickActionListener.delete();
        }
      }
    });
    quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
      @Override public void onDismiss() {
        Toast.makeText(activity, "Ups..dismissed", Toast.LENGTH_SHORT).show();
      }
    });
    quickAction.addActionItem(renameItem);
    quickAction.addActionItem(deleteItem);
  }

  public void show(View view, PlaylistQuickActionListener playlistQuickActionListener) {
    this.playlistQuickActionListener = playlistQuickActionListener;
    quickAction.show(view);
  }

  public void release() {
    this.activity = null;
    this.quickAction = null;
    this.playlistQuickActionListener = null;
  }
}
