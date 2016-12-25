package com.kingbull.musicplayer.ui.coverarts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

  List<String> coverUrls;

  public CoverArtsAdapter(List<String> imageUrls) {
    this.coverUrls = imageUrls;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Glide.with(holder.itemView.getContext())
        .load(coverUrls.get(position))
        .placeholder(R.drawable.a1)
        .error(R.drawable.default_art)
        .crossFade()
        .into(holder.imageView);
  }

  @Override public int getItemCount() {
    return coverUrls.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.label) TextView labelView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
    }
  }
}
