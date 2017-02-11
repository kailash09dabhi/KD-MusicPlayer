package com.kingbull.musicplayer.ui.main.categories.all;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.R;

/**
 * Created by KD on 2/10/2017.
 */

public final class SelectionContextOptionsLayout extends LinearLayout {

  @OnClick(R.id.clearSelectionsButton) void onClearSelectionsClick() {
    listener.onClearSelectionClick();
  }

  @OnClick(R.id.searchButton) void onSearchClick() {
    listener.onDeleteSelectedClick();
  }

  @OnClick(R.id.addToPlaylistButton) void onAddToPlaylistClick() {
    listener.onAddToPlaylistClick();
  }

  public SelectionContextOptionsLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private OnContextOptionClickListener listener;

  public void addOnContextOptionClickListener(OnContextOptionClickListener listener) {
    this.listener = listener;
  }

  private void init() {
    inflate(getContext(), R.layout.selection_context_options, this);
    ButterKnife.bind(this);
  }

  interface OnContextOptionClickListener {
    void onAddToPlaylistClick();

    void onDeleteSelectedClick();

    void onClearSelectionClick();
  }
}
