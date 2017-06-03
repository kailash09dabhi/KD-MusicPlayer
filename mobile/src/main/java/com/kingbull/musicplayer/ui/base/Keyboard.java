package com.kingbull.musicplayer.ui.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author Kailash Dabhi
 * @date 03 June, 2017 11:02 PM
 */
public final class Keyboard {
  public void hide(Activity activity) {
    InputMethodManager inputMethodManager =
        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
  }

  /**
   * Shows the soft keyboard
   */
  public void show(View view) {
    InputMethodManager inputMethodManager =
        (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    view.requestFocus();
    inputMethodManager.showSoftInput(view, 0);
  }
}
