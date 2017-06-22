package com.kingbull.musicplayer.domain.storage.sqlite;

import android.os.Parcel;
import android.os.Parcelable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaStatTable;
import java.util.List;
import javax.inject.Inject;

public final class FavouritesPlayList implements PlayList, Parcelable {
  public static final Creator<FavouritesPlayList> CREATOR = new Creator<FavouritesPlayList>() {
    @Override public FavouritesPlayList createFromParcel(Parcel in) {
      return new FavouritesPlayList(in);
    }

    @Override public FavouritesPlayList[] newArray(int size) {
      return new FavouritesPlayList[size];
    }
  };
  @Inject MediaStatTable mediaStatTable;

  protected FavouritesPlayList(Parcel in) {
    this();
  }

  public FavouritesPlayList() {
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public String name() {
    return "Private Favourites";
  }

  @Override public List<Music> musicList() {
    return mediaStatTable.favourites();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
  }
}
