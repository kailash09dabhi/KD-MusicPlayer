package com.kingbull.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Represents
 *
 * @author Kailash Dabhi
 * @date 7/29/2017 11:18 PM
 */
public interface ProLink {
  void open();

  class PlayStore implements ProLink {
    private final Context context;

    public PlayStore(Context context) {
      this.context = context;
    }

    @Override public void open() {
      final String appPackageName = context.getPackageName() + ".pro";
      try {
        context.startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
      } catch (android.content.ActivityNotFoundException anfe) {
        context.startActivity(new Intent(Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
      }
    }
  }
}
