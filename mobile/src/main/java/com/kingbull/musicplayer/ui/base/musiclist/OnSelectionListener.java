package com.kingbull.musicplayer.ui.base.musiclist;

public interface OnSelectionListener {
  OnSelectionListener NONE = new OnSelectionListener() {
    @Override public void onClearSelection() {
    }

    @Override public void onMultiSelection(int selectionCount) {
    }
  };

  void onClearSelection();

  void onMultiSelection(int selectionCount);
}