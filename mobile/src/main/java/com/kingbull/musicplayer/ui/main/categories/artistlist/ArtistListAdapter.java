package com.kingbull.musicplayer.ui.main.categories.artistlist;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Artist;
import com.kingbull.musicplayer.ui.main.categories.artistlist.artist.ArtistActivity;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> {
  private final List<Artist> artists;

  public ArtistListAdapter(List<Artist> artists) {
    this.artists = artists;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genres, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(artists.get(position).name());
  }

  @Override public int getItemCount() {
    return artists.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.textview) TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      Intent intent = new Intent(view.getContext(), ArtistActivity.class);
      intent.putExtra("artist", (Parcelable) artists.get(getAdapterPosition()));
      view.getContext().startActivity(intent);
    }
  }
}
