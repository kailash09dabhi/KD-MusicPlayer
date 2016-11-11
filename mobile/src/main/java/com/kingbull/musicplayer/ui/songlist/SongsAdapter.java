package com.kingbull.musicplayer.ui.songlist;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.ui.music.MusicPlayerFragment;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

  List<Song> songs;

  public SongsAdapter(List<Song> songs) {
    this.songs = songs;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genres, null));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(songs.get(position).getDisplayName());
  }

  @Override public int getItemCount() {
    return songs.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.textview) TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      ((FragmentActivity) view.getContext()).getSupportFragmentManager()
          .beginTransaction()
          .replace(android.R.id.content,
              MusicPlayerFragment.instance(songs.get(getAdapterPosition())))
          .addToBackStack(MusicPlayerFragment.class.getSimpleName())
          .commit();
    }
  }
}
