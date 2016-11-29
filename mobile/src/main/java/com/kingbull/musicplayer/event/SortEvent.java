package com.kingbull.musicplayer.event;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class SortEvent {

  private final @SortBy int sortBy;
  private final boolean sortInAscending;

  public SortEvent(@SortBy int sortBy, boolean sortInAscending) {
    this.sortBy = sortBy;
    this.sortInAscending = sortInAscending;
  }

  public int sortBy() {
    return sortBy;
  }

  public boolean isSortInAscending() {
    return sortInAscending;
  }

  @IntDef({
      SortBy.TITLE, SortBy.ARTIST, SortBy.ALBUM, SortBy.DURATION, SortBy.DATE_ADDED, SortBy.YEAR,
  }) @Retention(RetentionPolicy.SOURCE) public @interface SortBy {
    int TITLE = 0;
    int ARTIST = 1;
    int ALBUM = 2;
    int DURATION = 3;
    int DATE_ADDED = 4;
    int YEAR = 5;
  }
}
