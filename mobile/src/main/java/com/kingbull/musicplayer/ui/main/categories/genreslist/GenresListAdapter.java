package com.kingbull.musicplayer.ui.main.categories.genreslist;

import androidx.recyclerview.widget.RecyclerView;
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
public final class GenresListAdapter extends RecyclerView.Adapter<GenresListAdapter.ViewHolder> {
  private final List<Genre> genres;
  private final GenresList.Presenter presenter;

  public GenresListAdapter(List<Genre> genres, GenresList.Presenter presenter) {
    this.genres = genres;
    this.presenter = presenter;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genres, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(genres.get(position).name());
  }

  @Override public int getItemCount() {
    return genres.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.textview) TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      presenter.onGenreClick(genres.get(getAdapterPosition()));
    }
  }
}

