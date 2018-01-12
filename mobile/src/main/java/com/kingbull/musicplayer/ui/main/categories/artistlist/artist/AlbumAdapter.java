package com.kingbull.musicplayer.ui.main.categories.artistlist.artist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.image.GlideApp;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
  private final List<Album> albums;

  public AlbumAdapter(List<Album> albums) {
    this.albums = albums;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coverflow, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    GlideApp.with(holder.itemView.getContext())
        .load(albums.get(position).albumArt()).placeholder(R.drawable.ic_music_note)
        .error(R.drawable.bass_guitar)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(holder.imageView);
    holder.labelView.setText(albums.get(position).name());
  }

  @Override public int getItemCount() {
    return albums.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.label) TextView labelView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(final View view) {
    }
  }
}
