package com.kingbull.musicplayer.event;

import com.kingbull.musicplayer.domain.SortBy;

public final class SortEvent {
  private final @SortBy int sortBy;
  private final boolean sortInDescending;

  public SortEvent(@SortBy int sortBy, boolean sortInDescending) {
    this.sortBy = sortBy;
    this.sortInDescending = sortInDescending;
  }

  public @SortBy int sortBy() {
    return sortBy;
  }

  public boolean isSortInDescending() {
    return sortInDescending;
  }
}
