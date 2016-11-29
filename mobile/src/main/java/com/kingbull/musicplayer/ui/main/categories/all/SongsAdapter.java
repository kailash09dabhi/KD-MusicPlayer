package com.kingbull.musicplayer.ui.main.categories.all;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongFileViewHolder> {
  List<Music> songs;
  @Inject Player player;
  private SparseBooleanArray selectedItems = new SparseBooleanArray();

  public SongsAdapter(List<Music> songs) {
    this.songs = songs;
    MusicPlayerApp.instance().component().inject(this);
  }

  /**
   * Indicates if the item at position position is selected
   *
   * @param position Position of the item to check
   * @return true if the item is selected, false otherwise
   */
  public boolean isSelected(int position) {
    return getSelectedItems().contains(position);
  }

  /**
   * Toggle the selection status of the item at a given position
   *
   * @param position Position of the item to toggle the selection status for
   */
  public void toggleSelection(int position) {
    if (selectedItems.get(position, false)) {
      selectedItems.delete(position);
    } else {
      selectedItems.put(position, true);
    }
    notifyItemChanged(position);
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
  public void clearSelection() {
    List<Integer> selection = getSelectedItems();
    selectedItems.clear();
    for (Integer i : selection) {
      notifyItemChanged(i);
    }
  }

  /**
   * Count the selected items
   *
   * @return Selected items count
   */
  public int getSelectedItemCount() {
    return selectedItems.size();
  }

  /**
   * Indicates the list of selected items
   *
   * @return List of selected items ids
   */
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

  @Override public SongFileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new SongFileViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_song, parent, false));
  }

  @Override public void onBindViewHolder(SongFileViewHolder holder, int position) {
    if (isSelected(position)) {
      holder.itemView.setBackgroundColor(
          ContextCompat.getColor(holder.itemView.getContext(), R.color.transparent_strong_black));
    } else {
      holder.itemView.setBackgroundColor(
          ContextCompat.getColor(holder.itemView.getContext(), R.color.transparent));
    }
    Music music = songs.get(position);
    holder.fileNameView.setText(music.title());
    holder.albumView.setText(music.album());
    holder.durationView.setText(new Milliseconds(music.duration()).toMmSs());
  }

  @Override public int getItemCount() {
    return songs.size();
  }

  class SongFileViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {
    @BindView(R.id.fileName) TextView fileNameView;
    @BindView(R.id.durationView) TextView durationView;
    @BindView(R.id.artistView) TextView albumView;

    public SongFileViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    @Override public void onClick(final View view) {
      if (!isAnyItemSelected()) {
        player.addToNowPlaylist(songs);
        player.play(songs.get(getAdapterPosition()));
        view.getContext().startActivity(new Intent(view.getContext(), MusicPlayerActivity.class));
      } else {
        toggleSelection(getAdapterPosition());
      }
    }

    @Override public boolean onLongClick(View v) {
      toggleSelection(getAdapterPosition());
      return true;
    }
  }
}
