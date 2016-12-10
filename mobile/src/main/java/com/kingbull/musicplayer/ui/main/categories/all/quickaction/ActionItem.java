package com.kingbull.musicplayer.ui.main.categories.all.quickaction;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

class ActionItem {
  private Drawable icon;
  private Bitmap thumb;
  private String title;
  private int actionId = -1;
  private boolean selected;
  private boolean sticky;

  ActionItem(int actionId, String title, Drawable icon) {
    this.title = title;
    this.icon = icon;
    this.actionId = actionId;
  }

  public ActionItem() {
    this(-1, null, null);
  }

  public ActionItem(int actionId, String title) {
    this(actionId, title, null);
  }

  public ActionItem(Drawable icon) {
    this(-1, null, icon);
  }

  public ActionItem(int actionId, Drawable icon) {
    this(actionId, null, icon);
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Drawable getIcon() {
    return this.icon;
  }

  public void setIcon(Drawable icon) {
    this.icon = icon;
  }

  public int getActionId() {
    return actionId;
  }

  public void setActionId(int actionId) {
    this.actionId = actionId;
  }

  public boolean isSticky() {
    return sticky;
  }

  public void setSticky(boolean sticky) {
    this.sticky = sticky;
  }

  public boolean isSelected() {
    return this.selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public Bitmap getThumb() {
    return this.thumb;
  }

  public void setThumb(Bitmap thumb) {
    this.thumb = thumb;
  }
}