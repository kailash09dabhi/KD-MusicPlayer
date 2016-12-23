package com.kingbull.musicplayer.ui.main.categories.playlists;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import com.kingbull.musicplayer.ui.main.categories.playlists.members.MembersFragment;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class PlayListsAdapter extends RecyclerView.Adapter<PlayListsAdapter.ViewHolder> {

  List<PlayList> playLists;

  public PlayListsAdapter(List<PlayList> playLists) {
    this.playLists = playLists;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlists, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(playLists.get(position).name());
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
      imageview.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          new Snackbar(v).show("tapped!" + playLists.get(getAdapterPosition()));
        }
      });
      itemView.setOnClickListener(this);
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
