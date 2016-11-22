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
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.music.MusicPlayerFragment;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongFileViewHolder> {

  List<Music> songs;

  public SongsAdapter(List<Music> songs) {
    this.songs = songs;
  }

  @Override public SongFileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new SongFileViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_song, parent, false));
  }

  @Override public void onBindViewHolder(SongFileViewHolder holder, int position) {
    Music music = songs.get(position);
    holder.fileNameView.setText(music.title());
    holder.albumView.setText(music.album());
    holder.durationView.setText(new Milliseconds(music.duration()).toMmSs());
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
          .add(android.R.id.content, MusicPlayerFragment.instance(songs.get(getAdapterPosition())))
          .addToBackStack(MusicPlayerFragment.class.getSimpleName())
          .commit();
    }
  }

  class SongFileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.fileName) TextView fileNameView;
    @BindView(R.id.durationView) TextView durationView;
    @BindView(R.id.artistView) TextView albumView;

    public SongFileViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(final View view) {
      ((FragmentActivity) view.getContext()).getSupportFragmentManager()
          .beginTransaction()
          .add(android.R.id.content, MusicPlayerFragment.instance(songs.get(getAdapterPosition())))
          .addToBackStack(MusicPlayerFragment.class.getSimpleName())
          .commit();
    }
  }
}
