package com.kingbull.musicplayer.domain.storage.sqlite;

import android.os.Parcel;
import android.os.Parcelable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaStatTable;
import java.util.List;
import javax.inject.Inject;

public final class MostPlayedPlayList implements PlayList, Parcelable {

  public static final Creator<MostPlayedPlayList> CREATOR = new Creator<MostPlayedPlayList>() {
    @Override public MostPlayedPlayList createFromParcel(Parcel in) {
      return new MostPlayedPlayList(in);
    }

    @Override public MostPlayedPlayList[] newArray(int size) {
      return new MostPlayedPlayList[size];
    }
  };
  @Inject MediaStatTable mediaStatTable;

  public MostPlayedPlayList() {
    MusicPlayerApp.instance().component().inject(this);
  }

  protected MostPlayedPlayList(Parcel in) {
  }

  @Override public String name() {
    return "Most Played";
  }

  @Override public List<Music> musicList() {
    return mediaStatTable.mostPlayedSongs();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
  }
}
