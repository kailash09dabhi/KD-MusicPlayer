package com.kingbull.musicplayer.ui.base.musiclist;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.di.AppModule;
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.base.musiclist.edittags.EditTagsDialogFragment;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.ActionItem;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.QuickActionPopupWindow;
import com.kingbull.musicplayer.ui.base.musiclist.ringtone.Ringtone;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import com.kingbull.musicplayer.ui.statistics.StatisticsDialogFragment;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class MusicRecyclerViewAdapter
    extends RecyclerView.Adapter<MusicRecyclerViewAdapter.MusicViewHolder>
    implements FastScrollRecyclerView.SectionedAdapter {
  private final List<Music> songs;
  private final AppCompatActivity activity;
  private final SparseBooleanArray selectedItems = new SparseBooleanArray();
  @Inject Player player;
  private OnSelectionListener onSelectionListener = new OnSelectionListener() {
    @Override public void onClearSelection() {
    }

    @Override public void onMultiSelection(int selectionCount) {
    }
  };

  public MusicRecyclerViewAdapter(List<Music> songs, AppCompatActivity activity) {
    this.songs = songs;
    this.activity = activity;
    MusicPlayerApp.instance().component().inject(this);
  }

  public void addOnSelectionListener(OnSelectionListener onSelectionListener) {
    this.onSelectionListener = onSelectionListener;
  }

  /**
   * Toggle the selection status of the item at a given position
   *
   * @param position Position of the item to toggle the selection status for
   */
  private void toggleSelection(int position) {
    if (selectedItems.get(position, false)) {
      selectedItems.delete(position);
    } else {
      selectedItems.put(position, true);
    }
    if (getSelectedItemCount() == 0) {
      onSelectionListener.onClearSelection();
    } else {
      onSelectionListener.onMultiSelection(getSelectedItemCount());
    }
    notifyItemChanged(position);
  }

  private int getSelectedItemCount() {
    return selectedItems.size();
  }

  public boolean isAnyItemSelected() {
    boolean isAnySelected = false;
    for (int i = 0; i < selectedItems.size(); ++i) {
      isAnySelected = selectedItems.valueAt(i);
      if (isAnySelected) break;
    }
    return isAnySelected;
  }

  /**
   * Clear the selection status for all items
   */
  public final void clearSelection() {
    List<Integer> selection = getSelectedItems();
    selectedItems.clear();
    for (Integer i : selection) {
      notifyItemChanged(i);
    }
    onSelectionListener.onClearSelection();
  }

  private List<Integer> getSelectedItems() {
    List<Integer> items = new ArrayList<>(selectedItems.size());
    for (int i = 0; i < selectedItems.size(); ++i) {
      items.add(selectedItems.keyAt(i));
    }
    return items;
  }

  public List<SqlMusic> getSelectedMusics() {
    List<SqlMusic> items = new ArrayList<>(selectedItems.size());
    for (int i = 0; i < selectedItems.size(); ++i) {
      items.add((SqlMusic) songs.get(selectedItems.keyAt(i)));
    }
    return items;
  }

  @Override public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new MusicViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_music, parent, false));
  }

  @Override public void onBindViewHolder(final MusicViewHolder holder, int position) {
    if (isSelected(position)) {
      holder.itemView.setBackgroundColor(
          ContextCompat.getColor(holder.itemView.getContext(), R.color.transparent_strong_black));
    } else {
      holder.itemView.setBackgroundColor(
          ContextCompat.getColor(holder.itemView.getContext(), R.color.transparent));
    }
    final Music music = songs.get(position);
    holder.fileNameView.setText(music.media().title());
    holder.albumView.setText(music.media().album());
    holder.durationView.setText(new Milliseconds(music.media().duration()).toMmSs());
    holder.moreActionsView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(final View v) {
        QuickActionPopupWindow quickActionPopupWindow = new QuickActionPopupWindow(activity);
        int fillColor = new ColorTheme.Flat().header().intValue();
        final ActionItem playItem =
            new ActionItem("Play", new IconDrawable(R.drawable.ic_play_48dp, fillColor),
                new ActionItem.OnClickListener() {
                  @Override public void onClick(ActionItem item) {
                    player.addToNowPlaylist(songs);
                    player.play(songs.get(holder.getAdapterPosition()));
                    activity.startActivity(new Intent(activity, MusicPlayerActivity.class));
                  }
                });
        final ActionItem addToPlaylistItem = new ActionItem("Add To PlayList",
            new IconDrawable(R.drawable.ic_playlist_add_48dp, fillColor),
            new ActionItem.OnClickListener() {
              @Override public void onClick(ActionItem item) {
                List<SqlMusic> musicList = new ArrayList<>();
                musicList.add((SqlMusic) songs.get(holder.getAdapterPosition()));
                AddToPlayListDialogFragment.newInstance(musicList)
                    .show(activity.getSupportFragmentManager(),
                        AddToPlayListDialogFragment.class.getName());
              }
            });
        final ActionItem editTagsItem =
            new ActionItem("Edit Tags", new IconDrawable(R.drawable.ic_edit_48dp, fillColor),
                new ActionItem.OnClickListener() {
                  @Override public void onClick(ActionItem item) {
                    EditTagsDialogFragment.newInstance(songs.get(holder.getAdapterPosition()))
                        .show(activity.getSupportFragmentManager(),
                            EditTagsDialogFragment.class.getName());
                  }
                });
        final ActionItem statisticsItem = new ActionItem("Statistics",
            new IconDrawable(R.drawable.ic_info_outline_48dp, fillColor),
            new ActionItem.OnClickListener() {
              @Override public void onClick(ActionItem item) {
                StatisticsDialogFragment.newInstance(songs.get(holder.getAdapterPosition()))
                    .show(activity.getSupportFragmentManager(),
                        StatisticsDialogFragment.class.getName());
              }
            });
        final ActionItem setAsRingtoneItem = new ActionItem("Set As Ringtone",
            new IconDrawable(R.drawable.ic_ringtone_48dp, fillColor),
            new ActionItem.OnClickListener() {
              @Override public void onClick(ActionItem item) {
                new Ringtone(activity, songs.get(holder.getAdapterPosition())).set();
              }
            });
        final ActionItem deleteItem =
            new ActionItem("Delete", new IconDrawable(R.drawable.ic_delete_48dp, fillColor),
                new ActionItem.OnClickListener() {
                  @Override public void onClick(ActionItem item) {
                    new File(songs.get(holder.getAdapterPosition()).media().path()).delete();
                    activity.sendBroadcast(new Intent(Intent.ACTION_DELETE, Uri.fromFile(
                        new File(songs.get(holder.getAdapterPosition()).media().path()))));
                    new Snackbar(holder.itemView).show(String.format("%s has been deleted!",
                        songs.get(holder.getAdapterPosition()).media().title()));
                    songs.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                  }
                });
        final ActionItem sendItem =
            new ActionItem("Send", new IconDrawable(R.drawable.ic_send_48dp, fillColor),
                new ActionItem.OnClickListener() {
                  @Override public void onClick(ActionItem item) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("audio/*");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(
                        new File(songs.get(holder.getAdapterPosition()).media().path())));
                    activity.startActivity(Intent.createChooser(share, "Share Sound File"));
                  }
                });
        quickActionPopupWindow.addActionItem(playItem);
        quickActionPopupWindow.addActionItem(addToPlaylistItem);
        quickActionPopupWindow.addActionItem(editTagsItem);
        quickActionPopupWindow.addActionItem(statisticsItem);
        quickActionPopupWindow.addActionItem(setAsRingtoneItem);
        quickActionPopupWindow.addActionItem(deleteItem);
        quickActionPopupWindow.addActionItem(sendItem);
        quickActionPopupWindow.addOnActionClickListener(
            new QuickActionPopupWindow.OnActionClickListener() {
              @Override public void onActionClick(ActionItem actionItem) {
                actionItem.onClickListener().onClick(actionItem);
              }
            });
        quickActionPopupWindow.show(v);
      }
    });
  }

  private boolean isSelected(int position) {
    return getSelectedItems().contains(position);
  }

  @Override public int getItemCount() {
    return songs.size();
  }

  @NonNull @Override public String getSectionName(int position) {
    return String.valueOf(
        songs.get(position).media().title().substring(0, 1).toUpperCase(Locale.ENGLISH));
  }

  public interface OnSelectionListener {
    void onClearSelection();

    void onMultiSelection(int selectionCount);
  }

  public class MusicViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {
    @Inject @Named(AppModule.SMART_THEME) ColorTheme smartTheme;
    @BindView(R.id.fileName) TextView fileNameView;
    @BindView(R.id.durationView) TextView durationView;
    @BindView(R.id.artistView) TextView albumView;
    @BindView(R.id.moreActionsView) ImageView moreActionsView;

    public MusicViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      MusicPlayerApp.instance().component().inject(this);
      fileNameView.setTextColor(smartTheme.titleText().intValue());
      durationView.setTextColor(smartTheme.bodyText().intValue());
      albumView.setTextColor(smartTheme.bodyText().intValue());
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    @Override public final boolean onLongClick(View view) {
      toggleSelection(getAdapterPosition());
      return true;
    }

    @Override public void onClick(final View view) {
      if (getSelectedItemCount() > 0) {
        toggleSelection(getAdapterPosition());
      } else {
        player.addToNowPlaylist(songs);
        player.play(songs.get(getAdapterPosition()));
        view.getContext().startActivity(new Intent(view.getContext(), MusicPlayerActivity.class));
      }
    }
  }
}
