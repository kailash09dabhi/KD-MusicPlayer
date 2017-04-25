package com.kingbull.musicplayer.ui.base.musiclist.quickaction;

import android.graphics.drawable.Drawable;
import java.util.Objects;

public final class ActionItem {
  private final Drawable icon;
  private final String title;
  private final int actionId;
  private final boolean sticky = false;
  private final OnClickListener onClickListener;

  public ActionItem(int actionId, String title, Drawable icon) {
    this.title = title;
    this.icon = icon;
    this.actionId = actionId;
    this.onClickListener = null;
  }

  public ActionItem(int actionId, String title, Drawable icon, OnClickListener onClickListener) {
    this.title = title;
    this.icon = icon;
    this.actionId = actionId;
    this.onClickListener = onClickListener;
  }

  public ActionItem(String title, Drawable icon, OnClickListener onClickListener) {
    this.title = title;
    this.icon = icon;
    this.actionId = -1;
    this.onClickListener = onClickListener;
  }

  public String title() {
    return this.title;
  }

  public Drawable icon() {
    return this.icon;
  }

  public int actionId() {
    return actionId;
  }

  public boolean isSticky() {
    return sticky;
  }

  public OnClickListener onClickListener() {
    return onClickListener;
  }

  @Override public int hashCode() {
    return Objects.hash(icon, title, actionId, sticky, onClickListener);
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ActionItem that = (ActionItem) o;
    return actionId == that.actionId
        && sticky == that.sticky
        && Objects.equals(icon, that.icon)
        && Objects.equals(title, that.title)
        && Objects.equals(onClickListener, that.onClickListener);
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("ActionItem{");
    sb.append("icon=").append(icon);
    sb.append(", title='").append(title).append('\'');
    sb.append(", actionId=").append(actionId);
    sb.append(", sticky=").append(sticky);
    sb.append(", onClickListener=").append(onClickListener);
    sb.append('}');
    return sb.toString();
  }

  /**
   * Interface definition for a callback to be invoked when a ActionItem is clicked.
   */
  public interface OnClickListener {
    /**
     * Called when a ActionItem has been clicked.
     *
     * @param item The ActionItem that was clicked.
     */
    void onClick(ActionItem item);
  }
}