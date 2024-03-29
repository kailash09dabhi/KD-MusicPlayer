package com.kingbull.musicplayer.ui.main.categories.playlists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.ActionItem;
import com.kingbull.musicplayer.ui.base.musiclist.quickaction.QuickActionPopupWindow;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class AllPlaylistAdapter extends RecyclerView.Adapter<AllPlaylistAdapter.ViewHolder> {
  private final List<PlayList> playLists;
  private final AppCompatActivity activity;
  private final AllPlaylist.Presenter presenter;

  public AllPlaylistAdapter(List<PlayList> playLists, AppCompatActivity activity,
      AllPlaylist.Presenter presenter) {
    this.playLists = playLists;
    this.activity = activity;
    this.presenter = presenter;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlists, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    PlayList playList = playLists.get(position);
    holder.textView.setText(playLists.get(position).name());
    if (playList instanceof PlayList.Smart) {
      holder.imageview.setVisibility(View.VISIBLE);
    } else {
      holder.imageview.setVisibility(View.GONE);
    }
  }

  @Override public int getItemCount() {
    return playLists.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.nameView) TextView textView;
    @BindView(R.id.moreActionsView) ImageView imageview;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @OnClick(R.id.moreActionsView) void onMoreActionsClick(final View view) {
      QuickActionPopupWindow quickActionPopupWindow = new QuickActionPopupWindow(activity);
      int fillColor = new ColorTheme.Flat().header().intValue();
      final ActionItem renameItem =
          new ActionItem("Rename", new IconDrawable(R.drawable.ic_edit_48dp, fillColor),
                  item -> PlaylistRenameDialogFragment.newInstance(
                      (PlayList.Smart) playLists.get(getAdapterPosition()))
                      .show(activity.getSupportFragmentManager(),
                          PlaylistRenameDialogFragment.class.getName()));
      final ActionItem deleteItem =
          new ActionItem("Delete", new IconDrawable(R.drawable.ic_delete_48dp, fillColor),
                  item -> {
                    PlayList playList = playLists.get(getAdapterPosition());
                    if (playList instanceof PlayList.Smart) ((PlayList.Smart) playList).delete();
                    playLists.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                  });
      quickActionPopupWindow.addActionItem(renameItem);
      quickActionPopupWindow.addActionItem(deleteItem);
      quickActionPopupWindow.addOnActionClickListener(
              actionItem -> actionItem.onClickListener().onClick(actionItem));
      quickActionPopupWindow.show(view);
    }

    @Override public void onClick(View view) {
      presenter.onPlaylistClick(playLists.get(getAdapterPosition()));
    }
  }
}
