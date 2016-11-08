package com.kingbull.musicplayer.ui.main.songgroup.genres;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {

  List<Genre> songs;
  Typeface localTypeface;

  public GenresAdapter(List<Genre> songs) {
    this.songs = songs;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    localTypeface =
        Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/julius-sans-one.ttf");
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genres, null));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(songs.get(position).name());
  }

  @Override public int getItemCount() {
    return songs.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview) TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      textView.setTypeface(localTypeface);
    }
  }
}
