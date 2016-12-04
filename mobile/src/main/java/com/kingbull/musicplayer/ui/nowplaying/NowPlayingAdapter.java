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
import github.nisrulz.recyclerviewhelper.RVHAdapter;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.ViewHolder>
    implements RVHAdapter {

  List<Music> songs;
  @Inject Player player;

  public NowPlayingAdapter(List<Music> songs) {
    this.songs = songs;
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_now_playling2, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Music music = songs.get(position);
    holder.nameView.setText(music.media().title());
  }

  @Override public int getItemCount() {
    return songs.size();
  }

  @Override public boolean onItemMove(int fromPosition, int toPosition) {
    swap(fromPosition, toPosition);
    return false;
  }

  @Override public void onItemDismiss(int position, int direction) {
    remove(position);
  }

  private void remove(int position) {
    songs.remove(position);
    notifyItemRemoved(position);
  }

  private void swap(int firstPosition, int secondPosition) {
    Collections.swap(songs, firstPosition, secondPosition);
    notifyItemMoved(firstPosition, secondPosition);
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.nameView) TextView nameView;

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
}
