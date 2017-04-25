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
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.base.musiclist.edittags.EditTagsDialogFragment;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.ActionItem;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.QuickActionPopupWindow;
import com.kingbull.musicplayer.ui.base.musiclist.ringtone.Ringtone;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
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
  private final List<Music> songs;
  private final android.support.v4.app.FragmentManager fragmentManager;
  private final AppCompatActivity activity;
  private final SparseBooleanArray selectedItems = new SparseBooleanArray();
  private final PlayList playList;
  @Inject Player player;
  private OnSelectionListener onSelectionListener;

  public MembersRecyclerViewAdapter(PlayList playList, List<Music> songs,
      AppCompatActivity activity) {
    this.playList = playList;
    this.songs = songs;
    this.activity = activity;
    this.fragmentManager = activity.getSupportFragmentManager();
    MusicPlayerApp.instance().component().inject(this);
  }

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

  private boolean isAnyItemSelected() {
    boolean isAnySelected = false;
    for (int i = 0; i < selectedItems.size(); ++i) {
      isAnySelected = selectedItems.valueAt(i);
      if (isAnySelected) break;
    }
    return isAnySelected;
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

  @Override public void onBindViewHolder(final ViewHolder holder, int position) {
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
        final ActionItem addToPlaylistItem =
            new ActionItem("Move To", new IconDrawable(R.drawable.ic_playlist_add_48dp, fillColor),
                new ActionItem.OnClickListener() {
                  @Override public void onClick(ActionItem item) {
                    if (playList instanceof PlayList.Smart) {
                      MoveToDialogFragment.newInstance(playList,
                          songs.get(holder.getAdapterPosition()), holder.getAdapterPosition())
                          .show(fragmentManager, MoveToDialogFragment.class.getName());
                    }
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
        final ActionItem setAsRingtoneItem = new ActionItem("Set As Ringtone",
            new IconDrawable(R.drawable.ic_ringtone_48dp, fillColor),
            new ActionItem.OnClickListener() {
              @Override public void onClick(ActionItem item) {
                new Ringtone(activity, songs.get(holder.getAdapterPosition())).set();
              }
            });
        final ActionItem deleteItem = new ActionItem("Delete from playlist",
            new IconDrawable(R.drawable.ic_delete_48dp, fillColor),
            new ActionItem.OnClickListener() {
              @Override public void onClick(ActionItem item) {
                int adapterPosition = holder.getAdapterPosition();
                if (playList instanceof PlayList.Smart) {
                  ((PlayList.Smart) playList).remove(songs.get(adapterPosition));
                  songs.remove(adapterPosition);
                  notifyItemRemoved(adapterPosition);
                } else if (playList instanceof FavouritesPlayList) {
                  songs.get(adapterPosition).mediaStat().toggleFavourite();
                  songs.remove(adapterPosition);
                  notifyItemRemoved(adapterPosition);
                }
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
        quickActionPopupWindow.addActionItem(setAsRingtoneItem);
        if (playList instanceof PlayList.Smart || playList instanceof FavouritesPlayList) {
          quickActionPopupWindow.addActionItem(deleteItem);
        }
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

  private List<Integer> getSelectedItems() {
    List<Integer> items = new ArrayList<>(selectedItems.size());
    for (int i = 0; i < selectedItems.size(); ++i) {
      items.add(selectedItems.keyAt(i));
    }
    return items;
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

  private void clearSelection() {
    List<Integer> selection = getSelectedItems();
    selectedItems.clear();
    for (Integer i : selection) {
      notifyItemChanged(i);
    }
    onSelectionListener.onClearSelection();
  }

  public void addOnSelectionListener(OnSelectionListener onSelectionListener) {
    this.onSelectionListener = onSelectionListener;
  }

  interface OnSelectionListener {
    void onClearSelection();

    void onMultiSelection(int selectionCount);
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

    @Override public boolean onLongClick(View v) {
      if (playList instanceof PlayList.Smart || playList instanceof FavouritesPlayList) {
        toggleSelection(getAdapterPosition());
      }
      return true;
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
  }
}
