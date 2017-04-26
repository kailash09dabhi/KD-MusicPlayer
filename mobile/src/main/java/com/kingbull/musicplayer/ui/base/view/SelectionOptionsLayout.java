package com.kingbull.musicplayer.ui.base.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;

/**
 * @author Kailash Dabhi
 * @date 2/10/2017
 */
public final class SelectionOptionsLayout extends LinearLayout {
  @BindView(R.id.addToPlaylistButton) ImageView addToPlaylistButton;
  @BindView(R.id.deleteButton) ImageView deleteButton;
  @BindView(R.id.clearSelectionsButton) ImageView clearSelectionsButton;
  private OnContextOptionClickListener listener;

  public SelectionOptionsLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    inflate(getContext(), R.layout.selection_options, this);
    ButterKnife.bind(this);
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

  public void updateIconsColor(int fillColor) {
    addToPlaylistButton.setImageDrawable(
        new IconDrawable(R.drawable.ic_playlist_add_48dp, fillColor));
    deleteButton.setImageDrawable(new IconDrawable(R.drawable.ic_delete_48dp, fillColor));
    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_remote_view_close);
    drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    clearSelectionsButton.setImageDrawable(drawable);
  }

  public void updateIconSize(int size) {
    addToPlaylistButton.getLayoutParams().width = size;
    addToPlaylistButton.getLayoutParams().height = size;
    deleteButton.getLayoutParams().width = size;
    deleteButton.getLayoutParams().height = size;
    clearSelectionsButton.getLayoutParams().width = size;
    clearSelectionsButton.getLayoutParams().height = size;
  }

  public interface OnContextOptionClickListener {
    void onAddToPlaylistClick();

    void onDeleteSelectedClick();

    void onClearSelectionClick();
  }
}
