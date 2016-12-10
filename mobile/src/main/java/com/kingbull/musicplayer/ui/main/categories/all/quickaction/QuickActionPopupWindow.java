package com.kingbull.musicplayer.ui.main.categories.all.quickaction;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;
import com.kingbull.musicplayer.R;

/**
 * @author Kailash Dabhi
 * @date 12/8/2016.
 */

public class QuickActionPopupWindow {

  private final int ID_PLAY = 1;
  private final int ID_PLAYLIST = 2;
  private final int ID_EDIT_TAGS = 3;
  private final int ID_RINGTONE = 4;
  private final int ID_DELETE = 5;
  private final int ID_SEND = 6;

  private Activity activity;
  private QuickAction quickAction;
  private QuickActionListener quickActionListener;

  public QuickActionPopupWindow(final Activity activity) {
    this.activity = activity;
    final ActionItem playItem = new ActionItem(ID_PLAY, "Play",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_play));
    final ActionItem addToPlaylistItem = new ActionItem(ID_PLAYLIST, "Add To PlayList",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_multiselect));
    ActionItem editTagsItem = new ActionItem(ID_EDIT_TAGS, "Edit Tags",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_sort));
    final ActionItem setAsRingtoneItem = new ActionItem(ID_RINGTONE, "Set As Ringtone",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_shuffle));
    final ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_delete));
    ActionItem sendItem = new ActionItem(ID_SEND, "Send",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_queue));
    //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
    quickAction = new QuickAction(activity);
    //setup the action item click listener
    quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
      @Override public void onItemClick(QuickAction quickAction, int pos, int actionId) {
        ActionItem actionItem = quickAction.getActionItem(pos);
        if (actionId == ID_PLAY) {
          quickActionListener.play();
        } else if (actionId == ID_PLAYLIST) {
          quickActionListener.playlist();
        } else if (actionId == ID_EDIT_TAGS) {
          quickActionListener.editTags();
        } else if (actionId == ID_RINGTONE) {
          quickActionListener.ringtone();
        } else if (actionId == ID_DELETE) {
          quickActionListener.delete();
        } else if (actionId == ID_SEND) {
          quickActionListener.send();
        }
      }
    });
    quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
      @Override public void onDismiss() {
        Toast.makeText(activity, "Ups..dismissed", Toast.LENGTH_SHORT).show();
      }
    });
    quickAction.addActionItem(playItem);
    quickAction.addActionItem(addToPlaylistItem);
    quickAction.addActionItem(editTagsItem);
    quickAction.addActionItem(setAsRingtoneItem);
    quickAction.addActionItem(deleteItem);
    quickAction.addActionItem(sendItem);
  }

  public void show(View view, QuickActionListener quickActionListener) {
    this.quickActionListener = quickActionListener;
    quickAction.show(view);
  }

  public void release() {
    this.activity = null;
    this.quickAction = null;
    this.quickActionListener = null;
  }
}
