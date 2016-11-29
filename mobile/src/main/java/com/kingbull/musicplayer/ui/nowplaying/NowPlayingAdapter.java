package com.kingbull.musicplayer.ui.nowplaying;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class NowPlayingAdapter
    extends RecyclerView.Adapter<NowPlayingAdapter.SongFileViewHolder> {

  List<Music> songs;
  @Inject Player player;

  public NowPlayingAdapter(List<Music> songs) {
    this.songs = songs;
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public SongFileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new SongFileViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_now_playling, parent, false));
  }

  @Override public void onBindViewHolder(SongFileViewHolder holder, int position) {
    Music music = songs.get(position);
    holder.nameView.setText(music.title());
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
      player.play(songs.get(getAdapterPosition()));
      view.getContext().startActivity(new Intent(view.getContext(), MusicPlayerActivity.class));
    }
  }

  class SongFileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.nameView) TextView nameView;

    public SongFileViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(final View view) {
      player.play(songs.get(getAdapterPosition()));
      view.getContext().startActivity(new Intent(view.getContext(), MusicPlayerActivity.class));
    }
  }
}
