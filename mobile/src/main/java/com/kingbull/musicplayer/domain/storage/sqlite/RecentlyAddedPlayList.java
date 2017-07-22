package com.kingbull.musicplayer.domain.storage.sqlite;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.MusicGroup;
import com.kingbull.musicplayer.domain.MusicGroupOrder;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.domain.SortBy;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaTable;
import java.util.List;

public final class RecentlyAddedPlayList implements PlayList, Parcelable {
  public static final Creator<RecentlyAddedPlayList> CREATOR =
      new Creator<RecentlyAddedPlayList>() {
        @Override public RecentlyAddedPlayList createFromParcel(Parcel in) {
          return new RecentlyAddedPlayList(in);
        }

        @Override public RecentlyAddedPlayList[] newArray(int size) {
          return new RecentlyAddedPlayList[size];
        }
      };

  protected RecentlyAddedPlayList(Parcel in) {
    this();
  }

  public RecentlyAddedPlayList() {
  }

  @Override public String name() {
    return "Recently Added";
  }

  @Override public List<Music> musicList() {
    Cursor cursor = MusicPlayerApp.instance()
        .getContentResolver()
        .query(MediaTable.URI, MediaTable.projections(), null, null, null);
    List<Music> musicList = new MusicGroup.FromCursor(cursor).asList();
    new MusicGroupOrder(musicList).by(SortBy.DATE_ADDED);
    cursor.close();
    return musicList;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
  }
}