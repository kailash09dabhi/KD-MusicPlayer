package com.kingbull.musicplayer.domain.storage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kailash Dabhi on 04-09-2016.
 * You can contact us at kailash09dabhi@gmail.com OR on skype(kailash.09)
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
 */
public final class CurrentDateTime {
  private final SimpleDateFormat dateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

  public String toString() {
    Date date = new Date();
    return dateFormat.format(date);
  }
}
