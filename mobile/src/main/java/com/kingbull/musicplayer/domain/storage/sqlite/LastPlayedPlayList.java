package com.kingbull.musicplayer.domain.storage.sqlite;

import android.os.Parcel;
import android.os.Parcelable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.PlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaStatTable;
import java.util.List;
import javax.inject.Inject;

public final class LastPlayedPlayList implements PlayList, Parcelable {

  public static final Creator<LastPlayedPlayList> CREATOR = new Creator<LastPlayedPlayList>() {
    @Override public LastPlayedPlayList createFromParcel(Parcel in) {
      return new LastPlayedPlayList(in);
    }

    @Override public LastPlayedPlayList[] newArray(int size) {
      return new LastPlayedPlayList[size];
    }
  };
  @Inject MediaStatTable mediaStatTable;

  public LastPlayedPlayList() {
    MusicPlayerApp.instance().component().inject(this);
  }

  protected LastPlayedPlayList(Parcel in) {
  }

  @Override public String name() {
    return "Last Played";
  }

  @Override public List<Music> musicList() {
    return mediaStatTable.lastPlayedSongs();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
  }
}
