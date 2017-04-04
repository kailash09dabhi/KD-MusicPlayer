package com.kingbull.musicplayer.ui.main.categories.all;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;

/**
 * Created by KD on 2/10/2017.
 */
public final class SelectionContextOptionsLayout extends LinearLayout {
  @BindView(R.id.addToPlaylistButton) ImageView addToPlaylistButton;
  @BindView(R.id.deleteButton) ImageView deleteButton;
  private OnContextOptionClickListener listener;

  public SelectionContextOptionsLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  @OnClick(R.id.clearSelectionsButton) void onClearSelectionsClick() {
    listener.onClearSelectionClick();
  }

  @OnClick(R.id.deleteButton) void onDeleteClick() {
    listener.onDeleteSelectedClick();
  }

  @OnClick(R.id.addToPlaylistButton) void onAddToPlaylistClick() {
    listener.onAddToPlaylistClick();
  }

  public void addOnContextOptionClickListener(OnContextOptionClickListener listener) {
    this.listener = listener;
  }

  private void init() {
    inflate(getContext(), R.layout.selection_context_options, this);
    ButterKnife.bind(this);
  }

  public void updateIconsColor(int fillColor) {
    addToPlaylistButton.setImageDrawable(
        new IconDrawable(R.drawable.ic_playlist_add_48dp, Color.WHITE, fillColor));
    deleteButton.setImageDrawable(
        new IconDrawable(R.drawable.ic_delete_48dp, Color.WHITE, fillColor));
  }

  interface OnContextOptionClickListener {
    void onAddToPlaylistClick();

    void onDeleteSelectedClick();

    void onClearSelectionClick();
  }
}
