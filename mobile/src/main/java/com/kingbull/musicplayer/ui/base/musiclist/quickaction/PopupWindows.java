package com.kingbull.musicplayer.ui.base.musiclist.quickaction;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Custom popup window.
 *
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 */
class PopupWindows {
  protected Context context;
  protected PopupWindow popupWindow;
  protected View rootView;
  protected Drawable background = null;
  protected WindowManager windowManager;

  /**
   * Constructor.
   *
   * @param context Context
   */
   PopupWindows(Context context) {
     this.context = context;
     popupWindow = new PopupWindow(context);
     popupWindow.setTouchInterceptor(new OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
          popupWindow.dismiss();
          return true;
        }
        return false;
      }
    });
     windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
  }

  /**
   * On dismiss
   */
  protected void onDismiss() {
  }

  /**
   * On pre show
   */
  protected void preShow() {
    if (rootView == null) {
      throw new IllegalStateException("setContentView was not called with a view to display.");
    }
    onShow();
    if (background == null) {
      popupWindow.setBackgroundDrawable(new BitmapDrawable());
    } else {
      popupWindow.setBackgroundDrawable(background);
    }
    popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
    popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    popupWindow.setTouchable(true);
    popupWindow.setFocusable(true);
    popupWindow.setOutsideTouchable(true);
    popupWindow.setContentView(rootView);
  }

  /**
   * On show
   */
  protected void onShow() {
  }

  /**
   * Set background drawable.
   *
   * @param background Background drawable
   */
  public void setBackgroundDrawable(Drawable background) {
    this.background = background;
  }

  /**
   * Set content view.
   *
   * @param layoutResID Resource id
   */
  public void setContentView(int layoutResID) {
    LayoutInflater inflator =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    setContentView(inflator.inflate(layoutResID, null));
  }

  /**
   * Set content view.
   *
   * @param root Root view
   */
  public void setContentView(View root) {
    rootView = root;
    popupWindow.setContentView(root);
  }

  /**
   * Set listener on window dismissed.
   */
  public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
    popupWindow.setOnDismissListener(listener);
  }

  /**
   * Dismiss the popup window.
   */
  public void dismiss() {
    popupWindow.dismiss();
  }
}