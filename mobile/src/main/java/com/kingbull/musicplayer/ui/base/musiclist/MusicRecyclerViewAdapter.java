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
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.base.musiclist.edittags.EditTagsDialogFragment;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.MusicQuickAction;
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

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class MusicRecyclerViewAdapter
    extends RecyclerView.Adapter<MusicRecyclerViewAdapter.MusicViewHolder>
    implements FastScrollRecyclerView.SectionedAdapter {
  @Inject Player player;
  private List<Music> songs;
  private AppCompatActivity activity;
  private MusicQuickAction musicQuickAction;
  private SparseBooleanArray selectedItems = new SparseBooleanArray();
  private OnSelectionListener onSelectionListener = new OnSelectionListener() {
    @Override public void onClearSelection() {
    }

    @Override public void onMultiSelection(int selectionCount) {
    }
  };

  public MusicRecyclerViewAdapter(List<Music> songs, AppCompatActivity activity) {
    this.songs = songs;
    this.activity = activity;
    this.musicQuickAction = new MusicQuickAction(activity);
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
  public final void toggleSelection(int position) {
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

  public int getSelectedItemCount() {
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

  public List<Integer> getSelectedItems() {
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

  @Override public void onBindViewHolder(final MusicViewHolder holder, final int position) {
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
        musicQuickAction.show(v, new MusicQuickActionListener() {
          @Override public void play() {
            player.addToNowPlaylist(songs);
            player.play(songs.get(position));
            activity.startActivity(new Intent(activity, MusicPlayerActivity.class));
          }

          @Override public void playlist() {
            List<SqlMusic> musicList = new ArrayList<SqlMusic>();
            musicList.add((SqlMusic) songs.get(position));
            AddToPlayListDialogFragment.newInstance(musicList)
                .show(activity.getSupportFragmentManager(),
                    AddToPlayListDialogFragment.class.getName());
          }

          @Override public void editTags() {
            EditTagsDialogFragment.newInstance(songs.get(position))
                .show(activity.getSupportFragmentManager(), EditTagsDialogFragment.class.getName());
          }

          @Override public void statistics() {
            StatisticsDialogFragment.newInstance(songs.get(position))
                .show(activity.getSupportFragmentManager(),
                    StatisticsDialogFragment.class.getName());
          }

          @Override public void ringtone() {
            new Ringtone(activity, songs.get(position)).set();
          }

          @Override public void delete() {
            new File(songs.get(position).media().path()).delete();
            activity.sendBroadcast(new Intent(Intent.ACTION_DELETE,
                Uri.fromFile(new File(songs.get(position).media().path()))));
            new Snackbar(holder.itemView).show(
                String.format("%s has been deleted!", songs.get(position).media().title()));
            songs.remove(position);
            notifyItemRemoved(position);
          }

          @Override public void send() {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("audio/*");
            share.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(new File(songs.get(position).media().path())));
            activity.startActivity(Intent.createChooser(share, "Share Sound File"));
          }
        });
      }
    });
  }

  public boolean isSelected(int position) {
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

  class MusicViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {
    @BindView(R.id.fileName) TextView fileNameView;
    @BindView(R.id.durationView) TextView durationView;
    @BindView(R.id.artistView) TextView albumView;
    @BindView(R.id.moreActionsView) ImageView moreActionsView;
    ColorTheme smartTheme = new ColorTheme.Smart();

    public MusicViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
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
