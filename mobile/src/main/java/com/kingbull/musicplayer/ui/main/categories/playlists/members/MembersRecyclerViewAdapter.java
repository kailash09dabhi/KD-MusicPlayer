package com.kingbull.musicplayer.ui.main.categories.playlists.members;

import android.content.Intent;
import android.net.Uri;
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
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.FavouritesPlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.musiclist.edittags.EditTagsDialogFragment;
import com.kingbull.musicplayer.ui.base.musiclist.ringtone.Ringtone;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class MembersRecyclerViewAdapter
    extends RecyclerView.Adapter<MembersRecyclerViewAdapter.ViewHolder> {
  @Inject Player player;
  private List<Music> songs;
  private android.support.v4.app.FragmentManager fragmentManager;
  private AppCompatActivity activity;
  private MemberQuickAction playlistQuickAction;
  private MemberQuickAction quickActionWithDeleteOption;
  private SparseBooleanArray selectedItems = new SparseBooleanArray();
  private PlayList playList;
  private MemberQuickActionListener memberQuickActionListener;

  public MembersRecyclerViewAdapter(PlayList playList, List<Music> songs,
      AppCompatActivity activity) {
    this.playList = playList;
    this.songs = songs;
    this.activity = activity;
    this.fragmentManager = activity.getSupportFragmentManager();
    this.playlistQuickAction = new MemberQuickAction(activity, false);
    this.quickActionWithDeleteOption = new MemberQuickAction(activity, true);
    MusicPlayerApp.instance().component().inject(this);
  }

  public boolean isSelected(int position) {
    return getSelectedItems().contains(position);
  }

  public void toggleSelection(int position) {
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

  public boolean isAnyItemSelected() {
    boolean isAnySelected = false;
    for (int i = 0; i < selectedItems.size(); ++i) {
      isAnySelected = selectedItems.valueAt(i);
      if (isAnySelected) break;
    }
    return isAnySelected;
  }

  public void clearSelection() {
    List<Integer> selection = getSelectedItems();
    selectedItems.clear();
    for (Integer i : selection) {
      notifyItemChanged(i);
    }
    onSelectionListener.onClearSelection();
  }

  public int getSelectedItemCount() {
    return selectedItems.size();
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

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_music, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, final int position) {
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
        memberQuickActionListener = new MemberQuickActionListener() {
          @Override public void play() {
            player.addToNowPlaylist(songs);
            player.play(songs.get(position));
            activity.startActivity(new Intent(activity, MusicPlayerActivity.class));
          }

          @Override public void moveTo() {
            if (playList instanceof PlayList.Smart) {
              MoveToDialogFragment.newInstance(playList, songs.get(position), position)
                  .show(fragmentManager, MoveToDialogFragment.class.getName());
            }
          }

          @Override public void editTags() {
            EditTagsDialogFragment.newInstance(songs.get(position))
                .show(activity.getSupportFragmentManager(), EditTagsDialogFragment.class.getName());
          }

          @Override public void ringtone() {
            new Ringtone(activity, songs.get(position)).set();
          }

          @Override public void delete() {
            if (playList instanceof PlayList.Smart) {
              ((PlayList.Smart) playList).remove(songs.get(position));
              songs.remove(position);
              notifyItemRemoved(position);
            } else if (playList instanceof FavouritesPlayList) {
              songs.get(position).mediaStat().toggleFavourite();
              songs.remove(position);
              notifyItemRemoved(position);
            }
          }

          @Override public void send() {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("audio/*");
            share.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(new File(songs.get(position).media().path())));
            activity.startActivity(Intent.createChooser(share, "Share Sound File"));
          }
        };
        if (playList instanceof PlayList.Smart || playList instanceof FavouritesPlayList) {
          quickActionWithDeleteOption.show(v, memberQuickActionListener);
        } else {
          playlistQuickAction.show(v, memberQuickActionListener);
        }
      }
    });
  }

  @Override public int getItemCount() {
    return songs.size();
  }

  public void deleteSelectedMusicFromPlaylist() {
    List<Integer> positions = getSelectedItems();
    List<Music> deletableFiles = new ArrayList<>();
    if (playList instanceof PlayList.Smart) {
      for (Integer pos : positions) {
        Music music = songs.get(pos);
        ((PlayList.Smart) playList).remove(music);
        deletableFiles.add(music);
      }
    } else if (playList instanceof FavouritesPlayList) {
      for (Integer pos : positions) {
        Music music = songs.get(pos);
        music.mediaStat().toggleFavourite();
        deletableFiles.add(music);
      }
    }
    clearSelection();
    notifyItemRangeRemoved(0, songs.size());
    songs.removeAll(deletableFiles);
  }

  class ViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {
    @BindView(R.id.fileName) TextView fileNameView;
    @BindView(R.id.durationView) TextView durationView;
    @BindView(R.id.artistView) TextView albumView;
    @BindView(R.id.moreActionsView) ImageView moreActionsView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    @Override public void onClick(final View view) {
      if (playList instanceof PlayList.Smart || playList instanceof FavouritesPlayList) {
        if (!isAnyItemSelected()) {
          playSongsOfPlaylist(view);
        } else {
          toggleSelection(getAdapterPosition());
        }
      } else {
        playSongsOfPlaylist(view);
      }
    }

    private void playSongsOfPlaylist(View view) {
      player.addToNowPlaylist(songs);
      player.play(songs.get(getAdapterPosition()));
      view.getContext().startActivity(new Intent(view.getContext(), MusicPlayerActivity.class));
    }

    @Override public boolean onLongClick(View v) {
      if (playList instanceof PlayList.Smart || playList instanceof FavouritesPlayList) {
        toggleSelection(getAdapterPosition());
      }
      return true;
    }
  }

  private OnSelectionListener onSelectionListener;

  public void addOnSelectionListener(OnSelectionListener onSelectionListener) {
    this.onSelectionListener = onSelectionListener;
  }

  interface OnSelectionListener {
    void onClearSelection();

    void onMultiSelection(int selectionCount);
  }
}
