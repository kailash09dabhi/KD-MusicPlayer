package com.kingbull.musicplayer.ui.main.categories.albumlist;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.ui.main.categories.albumlist.album.AlbumActivity;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {

  List<Album> albumItems;

  public AlbumListAdapter(List<Album> albumItems) {
    this.albumItems = albumItems;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.labelView.setText(albumItems.get(position).name());
    Glide.with(holder.itemView.getContext())
        .load(albumItems.get(position).albumArt())
        .placeholder(R.drawable.a1)
        .error(R.drawable.default_art)
        .crossFade()
        .into(holder.imageView);
  }

  @Override public int getItemCount() {
    return albumItems.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.label) TextView labelView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      Intent intent = new Intent(view.getContext(), AlbumActivity.class);
      intent.putExtra("album_id", albumItems.get(getAdapterPosition()).albumId());
      intent.putExtra("title", albumItems.get(getAdapterPosition()).name());
      intent.putExtra("albumart", albumItems.get(getAdapterPosition()).albumArt());
      view.getContext().startActivity(intent);
    }
  }
}
