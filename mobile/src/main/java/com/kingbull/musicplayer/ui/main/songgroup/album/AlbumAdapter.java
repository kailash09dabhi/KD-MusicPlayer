package com.kingbull.musicplayer.ui.main.songgroup.album;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.songlist.SongListActivity;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

  List<AlbumItem> albumItems;

  public AlbumAdapter(List<AlbumItem> albumItems) {
    this.albumItems = albumItems;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genres, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(albumItems.get(position).name());
  }

  @Override public int getItemCount() {
    return albumItems.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.textview) TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      Intent intent = new Intent(view.getContext(), SongListActivity.class);
      intent.putExtra("album_id", albumItems.get(getAdapterPosition()).id());
      intent.putExtra("title", albumItems.get(getAdapterPosition()).name());
      view.getContext().startActivity(intent);
    }
  }
}
