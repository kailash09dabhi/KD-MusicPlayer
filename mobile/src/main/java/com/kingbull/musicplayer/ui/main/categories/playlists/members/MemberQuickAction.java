package com.kingbull.musicplayer.ui.main.categories.playlists.members;

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
public final class MemberQuickAction {
  private final int ID_PLAY = 1;
  private final int ID_MOVE_TO = 2;
  private final int ID_EDIT_TAGS = 3;
  private final int ID_RINGTONE = 4;
  private final int ID_DELETE = 5;
  private final int ID_SEND = 6;
  private Activity activity;
  private QuickAction quickAction;
  private MemberQuickActionListener memberQuickActionListener;
  private boolean hasDeleteOption;

  public MemberQuickAction(final Activity activity, boolean hasDeleteOption) {
    this.activity = activity;
    this.hasDeleteOption = hasDeleteOption;
    final ActionItem playItem = new ActionItem(ID_PLAY, "Play",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_play));
    final ActionItem addToPlaylistItem = new ActionItem(ID_MOVE_TO, "Move To",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_multiselect));
    final ActionItem editTagsItem = new ActionItem(ID_EDIT_TAGS, "Edit Tags",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_sort));
    final ActionItem setAsRingtoneItem = new ActionItem(ID_RINGTONE, "Set As Ringtone",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_shuffle));
    final ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete from playlist",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_delete));
    final ActionItem sendItem = new ActionItem(ID_SEND, "Send",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_queue));
    //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
    quickAction = new QuickAction(activity);
    //setup the action item click listener
    quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
      @Override public void onItemClick(QuickAction quickAction, int pos, int actionId) {
        ActionItem actionItem = quickAction.getActionItem(pos);
        if (actionId == ID_PLAY) {
          memberQuickActionListener.play();
        } else if (actionId == ID_MOVE_TO) {
          memberQuickActionListener.moveTo();
        } else if (actionId == ID_EDIT_TAGS) {
          memberQuickActionListener.editTags();
        } else if (actionId == ID_RINGTONE) {
          memberQuickActionListener.ringtone();
        } else if (actionId == ID_DELETE) {
          memberQuickActionListener.delete();
        } else if (actionId == ID_SEND) {
          memberQuickActionListener.send();
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
    if (hasDeleteOption) {
      quickAction.addActionItem(deleteItem);
    }
    quickAction.addActionItem(sendItem);
  }

  public void show(View view, MemberQuickActionListener memberQuickActionListener) {
    this.memberQuickActionListener = memberQuickActionListener;
    quickAction.show(view);
  }

  public void release() {
    this.activity = null;
    this.quickAction = null;
    this.memberQuickActionListener = null;
  }
}
