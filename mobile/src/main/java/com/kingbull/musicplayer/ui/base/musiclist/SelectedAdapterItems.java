package com.kingbull.musicplayer.ui.base.musiclist;

import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents selected list items.
 *
 * @author Kailash Dabhi
 * @date 7/27/2017 11:24 AM
 */
public final class SelectedAdapterItems {
  private final SparseBooleanArray selectedItems = new SparseBooleanArray();
  private final RecyclerView.Adapter adapter;
  private OnSelectionListener onSelectionListener;

  public SelectedAdapterItems(RecyclerView.Adapter adapter) {
    this.adapter = adapter;
  }

  public void addOnSelectionListener(
      OnSelectionListener onSelectionListener) {
    this.onSelectionListener = onSelectionListener;
  }

  public boolean has(int position) {
    return selectedItems.get(position);
  }


  /**
   * Either puts the position or delete the position if it is has.
   *
   * @param position Position of the item to toggle the selection status
   */
  void toggleSelection(int position) {
    if (selectedItems.get(position, false)) {
      selectedItems.delete(position);
    } else {
      selectedItems.put(position, true);
    }
    if (count() == 0) {
      onSelectionListener.onClearSelection();
    } else {
      onSelectionListener.onMultiSelection(count());
    }
    adapter.notifyItemChanged(position);
  }

  int count() {
    return selectedItems.size();
  }

  /**
   * Clear the selection status for all items.
   */
  public final void removeAll() {
    int[] selectionIndexes = selectedIndexes();
    selectedItems.clear();
    for (int index : selectionIndexes) {
      adapter.notifyItemChanged(index);
    }
    onSelectionListener.onClearSelection();
  }

  @VisibleForTesting int[] selectedIndexes() {
    int[] items = new int[selectedItems.size()];
    int size = items.length;
    for (int i = 0; i < size; ++i) {
      items[i] = selectedItems.keyAt(i);
    }
    return items;
  }

  public <T> List<T> of(List<T> list) {
    List<T> items = new ArrayList<T>(count());
    for (int i = 0; i < count(); ++i) {
      items.add(list.get(selectedItems.keyAt(i)));
    }
    return items;
  }
}
