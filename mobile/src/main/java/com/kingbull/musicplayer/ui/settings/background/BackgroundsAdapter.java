package com.kingbull.musicplayer.ui.settings.background;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.main.Pictures;
import com.kingbull.musicplayer.ui.settings.background.BackgroundsDialogFragment.OnClickListener;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class BackgroundsAdapter extends RecyclerView.Adapter<BackgroundsAdapter.ViewHolder> {
  private final int[] pictures = new Pictures().toDrawablesId();
  private final OnClickListener onClickListener;

  public BackgroundsAdapter(OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Glide.with(holder.imageView.getContext()).load(pictures[position]).into(holder.imageView);
  }

  @Override public int getItemCount() {
    return pictures.length;
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.imageView) ImageView imageView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (getAdapterPosition() != RecyclerView.NO_POSITION) {
        onClickListener.onClick(getAdapterPosition());
      }
    }
  }
}

