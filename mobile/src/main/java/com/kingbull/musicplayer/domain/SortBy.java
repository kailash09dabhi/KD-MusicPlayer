package com.kingbull.musicplayer.domain;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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