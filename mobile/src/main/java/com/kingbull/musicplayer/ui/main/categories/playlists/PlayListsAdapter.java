package com.kingbull.musicplayer.ui.main.categories.playlists;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
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
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genres, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(playLists.get(position).name());
  }

  @Override public int getItemCount() {
    return playLists.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.textview) TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      textView.setTransformationMethod(null);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
    }
  }
}
