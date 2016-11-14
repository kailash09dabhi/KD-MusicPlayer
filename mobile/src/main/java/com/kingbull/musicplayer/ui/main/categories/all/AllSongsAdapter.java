package com.kingbull.musicplayer.ui.main.categories.all;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.ui.songlist.SongListActivity;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class AllSongsAdapter extends RecyclerView.Adapter<AllSongsAdapter.ViewHolder> {

  List<Song> songs;

  public AllSongsAdapter(List<Song> songs) {
    this.songs = songs;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genres, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(songs.get(position).getTitle());
  }

  @Override public int getItemCount() {
    return songs.size();
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
      Intent intent = new Intent(view.getContext(), SongListActivity.class);
      intent.putExtra("genre_id", songs.get(getAdapterPosition()).getId());
      intent.putExtra("title", songs.get(getAdapterPosition()).getTitle());
      view.getContext().startActivity(intent);
    }
  }
}
