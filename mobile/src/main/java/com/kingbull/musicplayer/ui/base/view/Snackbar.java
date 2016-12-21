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
    android.support.design.widget.Snackbar snackbar =
        android.support.design.widget.Snackbar.make(view, message,
            android.support.design.widget.Snackbar.LENGTH_LONG);
    snackbar.getView().setBackgroundColor(Color.parseColor("#99000000"));
    snackbar.show();
  }
}
