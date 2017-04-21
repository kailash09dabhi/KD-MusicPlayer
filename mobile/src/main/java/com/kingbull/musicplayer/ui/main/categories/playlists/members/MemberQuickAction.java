package com.kingbull.musicplayer.ui.main.categories.playlists.members;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.ActionItem;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.QuickAction;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;

/**
 * @author Kailash Dabhi
 * @date 12/8/2016.
 */
final class MemberQuickAction {
  private final int ID_PLAY = 1;
  private final int ID_MOVE_TO = 2;
  private final int ID_EDIT_TAGS = 3;
  private final int ID_RINGTONE = 4;
  private final int ID_DELETE = 5;
  private final int ID_SEND = 6;
  private final boolean hasDeleteOption;
  private Activity activity;
  private QuickAction quickAction;
  private MemberQuickActionListener memberQuickActionListener;

  public MemberQuickAction(final Activity activity, boolean hasDeleteOption) {
    this.activity = activity;
    this.hasDeleteOption = hasDeleteOption;
    int fillColor = new ColorTheme.Flat().header().intValue();
    final ActionItem playItem = new ActionItem(ID_PLAY, "Play",
        new IconDrawable(R.drawable.ic_play_48dp, fillColor));
    final ActionItem addToPlaylistItem = new ActionItem(ID_MOVE_TO, "Move To",
        new IconDrawable(R.drawable.ic_playlist_add_48dp, fillColor));
    final ActionItem editTagsItem = new ActionItem(ID_EDIT_TAGS, "Edit Tags",
        new IconDrawable(R.drawable.ic_edit_48dp, fillColor));
    final ActionItem setAsRingtoneItem = new ActionItem(ID_RINGTONE, "Set As Ringtone",
        new IconDrawable(R.drawable.ic_ringtone_48dp, fillColor));
    final ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete from playlist",
        new IconDrawable(R.drawable.ic_delete_48dp, fillColor));
    final ActionItem sendItem = new ActionItem(ID_SEND, "Send",
        new IconDrawable(R.drawable.ic_send_48dp, fillColor));
    //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
    quickAction = new QuickAction(activity);
    //setup the action item click listener
    quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
      @Override public void onItemClick(QuickAction quickAction, int pos, int actionId) {
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
