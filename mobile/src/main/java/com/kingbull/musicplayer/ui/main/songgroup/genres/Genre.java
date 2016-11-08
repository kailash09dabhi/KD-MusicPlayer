package com.kingbull.musicplayer.ui.main.songgroup.genres;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class Genre {
  private String _id;
  private String name;

  public void setId(String _id) {
    this._id = _id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String id() {
    return _id;
  }

  public String name() {
    return name;
  }
}
