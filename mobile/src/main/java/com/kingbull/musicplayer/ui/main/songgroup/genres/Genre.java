package com.kingbull.musicplayer.ui.main.songgroup.genres;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class Genre {
  private int  _id;
  private String name;

  public void setId(int _id) {
    this._id = _id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int id() {
    return _id;
  }

  public String name() {
    return name;
  }
}
