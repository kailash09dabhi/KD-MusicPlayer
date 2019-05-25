package com.kingbull.musicplayer.ui.main.categories.artistlist;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Artist;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> {
  private final List<Artist> artists;
  private final com.kingbull.musicplayer.ui.main.categories.artistlist.ArtistList.Presenter
      presenter;

  public ArtistListAdapter(List<Artist> artists,
      com.kingbull.musicplayer.ui.main.categories.artistlist.ArtistList.Presenter presenter) {
    this.artists = artists;
    this.presenter = presenter;
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
      presenter.onArtistClick(artists.get(getAdapterPosition()));
    }
  }
}
