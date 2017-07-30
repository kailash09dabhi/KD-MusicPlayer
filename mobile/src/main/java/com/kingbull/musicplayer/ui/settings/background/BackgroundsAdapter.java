package com.kingbull.musicplayer.ui.settings.background;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.kingbull.musicplayer.BuildConfig;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import com.kingbull.musicplayer.ui.main.Pictures;
import com.kingbull.musicplayer.ui.settings.background.BackgroundsDialogFragment.OnBackgroundSelectionListener;
import com.lid.lib.LabelImageView;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class BackgroundsAdapter extends RecyclerView.Adapter<BackgroundsAdapter.ViewHolder> {
  private final int[] pictures = new Pictures().toDrawablesId();
  private final OnBackgroundSelectionListener onBackgroundSelectionListener;
  private final ColorTheme colorTheme = new ColorTheme.Flat();
  private final int proStartIndex;

  public BackgroundsAdapter(OnBackgroundSelectionListener onBackgroundSelectionListener,
      int proStartIndex) {
    this.onBackgroundSelectionListener = onBackgroundSelectionListener;
    this.proStartIndex = proStartIndex;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Glide.with(holder.imageView.getContext()).load(pictures[position]).into(holder.imageView);
    if (BuildConfig.FLAVOR.equals("free")) {
      if (proStartIndex >= position) {
        holder.imageView.setLabelVisual(false);
      } else {
        holder.imageView.setLabelVisual(true);
      }
    }
  }

  @Override public int getItemCount() {
    return pictures.length;
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.imageView) LabelImageView imageView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      imageView.setLabelBackgroundColor(colorTheme.header().intValue());
      imageView.setLabelVisual(false);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (getAdapterPosition() != RecyclerView.NO_POSITION) {
        onBackgroundSelectionListener.onBackgroundSelection(getAdapterPosition());
      }
    }
  }
}

