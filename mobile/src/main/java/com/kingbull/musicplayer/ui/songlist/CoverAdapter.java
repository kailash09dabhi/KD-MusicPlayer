package com.kingbull.musicplayer.ui.songlist;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.utils.AlbumUtils;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.SongFileViewHolder> {

  List<Music> songs;

  public CoverAdapter(List<Music> songs) {
    this.songs = songs;
  }

  @Override public SongFileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new SongFileViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coverflow, parent, false));
  }

  @Override public void onBindViewHolder(SongFileViewHolder holder, int position) {
    Bitmap bitmap = AlbumUtils.parseAlbum(songs.get(position));
    if (bitmap == null) {
      holder.imageView.setImageResource(R.drawable.default_record_album);
    } else {
      holder.imageView.setImageBitmap(bitmap);
    }
    holder.labelView.setText(songs.get(position).media().album());
  }

  @Override public int getItemCount() {
    return songs.size();
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
