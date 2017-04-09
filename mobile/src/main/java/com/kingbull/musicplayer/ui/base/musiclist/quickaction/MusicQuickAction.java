package com.kingbull.musicplayer.ui.base.musiclist.quickaction;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
  private QuickAction quickAction;
  private MusicQuickActionListener musicQuickActionListener;

  public MusicQuickAction(final Activity activity) {
    this.activity = activity;
    int fillColor = new ColorTheme.Flat().header().intValue();
    final ActionItem playItem = new ActionItem(ID_PLAY, "Play",
        ContextCompat.getDrawable(activity, R.drawable.ic_play));
    final ActionItem addToPlaylistItem = new ActionItem(ID_PLAYLIST, "Add To PlayList",
        new IconDrawable(R.drawable.ic_playlist_add_48dp, Color.WHITE, fillColor));
    ActionItem editTagsItem = new ActionItem(ID_EDIT_TAGS, "Edit Tags",
        new IconDrawable(R.drawable.ic_edit_48dp, Color.WHITE, fillColor));
    ActionItem statisticsItem = new ActionItem(ID_STATISTICS, "Statistics",
        ContextCompat.getDrawable(activity, R.drawable.composer_icn_search));
    final ActionItem setAsRingtoneItem = new ActionItem(ID_RINGTONE, "Set As Ringtone",
        ContextCompat.getDrawable(activity, R.drawable.composer_button_shuffle));
    final ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete",
        new IconDrawable(R.drawable.ic_delete_48dp, Color.WHITE, fillColor));
    ActionItem sendItem = new ActionItem(ID_SEND, "Send",
        new IconDrawable(R.drawable.ic_send_48dp, Color.WHITE, fillColor));
    //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
    quickAction = new QuickAction(activity);
    //setup the action item click listener
    quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
      @Override public void onItemClick(QuickAction quickAction, int pos, int actionId) {
        ActionItem actionItem = quickAction.getActionItem(pos);
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
    quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
      @Override public void onDismiss() {
        //Toast.makeText(activity, "Ups..dismissed", Toast.LENGTH_SHORT)y.show();
      }
    });
    quickAction.addActionItem(playItem);
    quickAction.addActionItem(addToPlaylistItem);
    quickAction.addActionItem(editTagsItem);
    quickAction.addActionItem(statisticsItem);
    quickAction.addActionItem(setAsRingtoneItem);
    quickAction.addActionItem(deleteItem);
    quickAction.addActionItem(sendItem);
  }

  public void show(View view, MusicQuickActionListener musicQuickActionListener) {
    this.musicQuickActionListener = musicQuickActionListener;
    quickAction.show(view);
  }

  public void release() {
    this.activity = null;
    this.quickAction = null;
    this.musicQuickActionListener = null;
  }
}
