package com.kingbull.musicplayer.ui.main.categories.playlists;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.main.categories.playlists.members.MembersFragment;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class PlayListsAdapter extends RecyclerView.Adapter<PlayListsAdapter.ViewHolder> {
  private final PlaylistQuickAction playlistQuickAction;
  private List<PlayList> playLists;
  private AppCompatActivity activity;

  public PlayListsAdapter(List<PlayList> playLists, AppCompatActivity activity) {
    this.playLists = playLists;
    this.activity = activity;
    this.playlistQuickAction = new PlaylistQuickAction(activity);
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
      playlistQuickAction.show(view, new PlaylistQuickActionListener() {
        @Override public void rename() {
          PlaylistRenameDialogFragment.newInstance(
              (PlayList.Smart) playLists.get(getAdapterPosition()))
              .show(activity.getSupportFragmentManager(),
                  PlaylistRenameDialogFragment.class.getName());
        }

        @Override public void delete() {
          PlayList playList = playLists.get(getAdapterPosition());
          if (playList instanceof PlayList.Smart) ((PlayList.Smart) playList).delete();
          playLists.remove(getAdapterPosition());
          notifyItemRemoved(getAdapterPosition());
        }
      });
    }

    @Override public void onClick(View view) {
      ((BaseActivity) view.getContext()).getSupportFragmentManager()
          .beginTransaction()
          .add(android.R.id.content,
              MembersFragment.newInstance(playLists.get(getAdapterPosition())),
              MembersFragment.class.getSimpleName())
          .addToBackStack(MembersFragment.class.getSimpleName())
          .commit();
    }
  }
}
