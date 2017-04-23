package com.kingbull.musicplayer.ui.main.categories.genreslist.genre;

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
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.SongFileViewHolder> {
  private final List<Album> albums;

  public CoverAdapter(List<Album> albums) {
    this.albums = albums;
  }

  @Override public SongFileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new SongFileViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coverflow, parent, false));
  }

  @Override public void onBindViewHolder(SongFileViewHolder holder, int position) {
    Glide.with(holder.itemView.getContext())
        .load(albums.get(position).albumArt()).placeholder(R.drawable.k10).error(R.drawable.k1)
        .crossFade()
        .into(holder.imageView);
    holder.labelView.setText(albums.get(position).name());
  }

  @Override public int getItemCount() {
    return albums.size();
  }

  class SongFileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.label) TextView labelView;

    public SongFileViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(final View view) {
    }
  }
}
