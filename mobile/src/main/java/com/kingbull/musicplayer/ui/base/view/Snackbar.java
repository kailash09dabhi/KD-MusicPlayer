package com.kingbull.musicplayer.ui.base.view;

import android.graphics.Color;
import android.view.View;

/**
 * @author Kailash Dabhi
 * @date 12/15/2016.
 */

public final class Snackbar {
  private final View view;

  public Snackbar(View view) {
    this.view = view;
  }

  public void show(String message) {
    com.google.android.material.snackbar.Snackbar snackbar =
        com.google.android.material.snackbar.Snackbar.make(view, message,
            com.google.android.material.snackbar.Snackbar.LENGTH_LONG);
    snackbar.getView().setBackgroundColor(Color.parseColor("#bb000000"));
    snackbar.show();
  }
}
