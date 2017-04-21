package com.kingbull.musicplayer.domain.storage.sqlite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Kailash Dabhi
 * @date 04 Sept, 2016
 */
public final class CurrentDateTime {
  private final SimpleDateFormat dateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

  public String toString() {
    Date date = new Date();
    return dateFormat.format(date);
  }
}
