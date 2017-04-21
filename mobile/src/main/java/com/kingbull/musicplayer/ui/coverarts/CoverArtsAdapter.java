package com.kingbull.musicplayer.ui.coverarts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.kingbull.musicplayer.R;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class CoverArtsAdapter extends RecyclerView.Adapter<CoverArtsAdapter.ViewHolder> {
  private static final int NO_POSITION = -1;
  private static int selectedPosition = NO_POSITION;
  private List<String> coverUrls;
  private CoverArts.Presenter presenter;

  public CoverArtsAdapter(List<String> imageUrls) {
    this.coverUrls = imageUrls;
  }

  public void takePresenter(CoverArts.Presenter presenter) {
    this.presenter = presenter;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coverart, parent, false));
  }

  @Override public void onBindViewHolder(final ViewHolder holder, int position) {
    Glide.with(holder.itemView.getContext())
        .load(coverUrls.get(position))
        .placeholder(R.drawable.a1)
        .error(R.drawable.default_art)
        .crossFade()
        .into(holder.imageView);
    if (selectedPosition == position) {
      holder.hoverView.setVisibility(View.VISIBLE);
    } else {
      holder.hoverView.setVisibility(View.GONE);
    }
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        notifyItemChanged(selectedPosition);
        selectedPosition = holder.getAdapterPosition();
        holder.hoverView.setVisibility(View.VISIBLE);
      }
    });
    holder.saveButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        presenter.onSaveCoverArtClick(coverUrls.get(holder.getAdapterPosition()));
      }
    });
  }

  @Override public int getItemCount() {
    return coverUrls.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.label) TextView labelView;
    @BindView(R.id.hoverView) View hoverView;
    @BindView(R.id.saveButton) Button saveButton;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
