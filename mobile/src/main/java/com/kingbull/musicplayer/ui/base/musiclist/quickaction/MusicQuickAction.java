package com.kingbull.musicplayer.ui.base.musiclist.quickaction;

import android.app.Activity;
import android.view.View;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.base.musiclist.MusicQuickActionListener;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;

/**
 * @author Kailash Dabhi
 * @date 12/8/2016.
 */
public final class MusicQuickAction {
  private final int ID_PLAY = 1;
  private final int ID_PLAYLIST = 2;
  private final int ID_EDIT_TAGS = 3;
  private final int ID_STATISTICS = 4;
  private final int ID_RINGTONE = 5;
  private final int ID_DELETE = 6;
  private final int ID_SEND = 7;
  private Activity activity;
  private QuickActionPopupWindow quickActionPopupWindow;
  private MusicQuickActionListener musicQuickActionListener;

  public MusicQuickAction(final Activity activity) {
    this.activity = activity;
    int fillColor = new ColorTheme.Flat().header().intValue();
    final ActionItem playItem = new ActionItem(ID_PLAY, "Play",
        new IconDrawable(R.drawable.ic_play_48dp, fillColor));
    final ActionItem addToPlaylistItem = new ActionItem(ID_PLAYLIST, "Add To PlayList",
        new IconDrawable(R.drawable.ic_playlist_add_48dp, fillColor));
    final ActionItem editTagsItem = new ActionItem(ID_EDIT_TAGS, "Edit Tags",
        new IconDrawable(R.drawable.ic_edit_48dp, fillColor));
    final ActionItem statisticsItem = new ActionItem(ID_STATISTICS, "Statistics",
        new IconDrawable(R.drawable.ic_info_outline_48dp, fillColor));
    final ActionItem setAsRingtoneItem = new ActionItem(ID_RINGTONE, "Set As Ringtone",
        new IconDrawable(R.drawable.ic_ringtone_48dp, fillColor));
    final ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete",
        new IconDrawable(R.drawable.ic_delete_48dp, fillColor));
    final ActionItem sendItem = new ActionItem(ID_SEND, "Send",
        new IconDrawable(R.drawable.ic_send_48dp, fillColor));
    //use isSticky(true) to disable QuickActionPopupWindow dialog being dismissed after an item is clicked
    quickActionPopupWindow = new QuickActionPopupWindow(activity);
    //setup the action item click listener
    quickActionPopupWindow.setOnActionItemClickListener(
        new QuickActionPopupWindow.OnActionItemClickListener() {
          @Override public void onItemClick(QuickActionPopupWindow quickActionPopupWindow, int pos,
              int actionId) {
        if (actionId == ID_PLAY) {
          musicQuickActionListener.play();
        } else if (actionId == ID_PLAYLIST) {
          musicQuickActionListener.playlist();
        } else if (actionId == ID_EDIT_TAGS) {
          musicQuickActionListener.editTags();
        } else if (actionId == ID_STATISTICS) {
          musicQuickActionListener.statistics();
        } else if (actionId == ID_RINGTONE) {
          musicQuickActionListener.ringtone();
        } else if (actionId == ID_DELETE) {
          musicQuickActionListener.delete();
        } else if (actionId == ID_SEND) {
          musicQuickActionListener.send();
        }
      }
    });
    quickActionPopupWindow.setOnDismissListener(new QuickActionPopupWindow.OnDismissListener() {
      @Override public void onDismiss() {
      }
    });
    quickActionPopupWindow.addActionItem(playItem);
    quickActionPopupWindow.addActionItem(addToPlaylistItem);
    quickActionPopupWindow.addActionItem(editTagsItem);
    quickActionPopupWindow.addActionItem(statisticsItem);
    quickActionPopupWindow.addActionItem(setAsRingtoneItem);
    quickActionPopupWindow.addActionItem(deleteItem);
    quickActionPopupWindow.addActionItem(sendItem);
  }

  public void show(View view, MusicQuickActionListener musicQuickActionListener) {
    this.musicQuickActionListener = musicQuickActionListener;
    quickActionPopupWindow.show(view);
  }

  public void release() {
    this.activity = null;
    this.quickActionPopupWindow = null;
    this.musicQuickActionListener = null;
  }
}
