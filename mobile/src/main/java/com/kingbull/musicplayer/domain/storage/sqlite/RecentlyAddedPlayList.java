package com.kingbull.musicplayer.domain.storage.sqlite;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

  public RecentlyAddedPlayList() {
  }

  protected RecentlyAddedPlayList(Parcel in) {
  }

  @Override public String name() {
    return "Recently Added";
  }

  @Override public List<Music> musicList() {
    Cursor cursor = MusicPlayerApp.instance()
        .getContentResolver()
        .query(MediaTable.URI, MediaTable.projections(), null, null, null);
    List<Music> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          SqlMusic song = new SqlMusic(new Media.Smart(cursor));
          itemList.add(song);
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
    Collections.sort(itemList, new RecentlyAddedComparator());
    return itemList;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
  }

  final class RecentlyAddedComparator implements Comparator<Music> {

    @Override public int compare(Music song1, Music song2) {
      long dateAddedSong1 = song1.media().dateAdded();
      long dateAddedSong2 = song2.media().dateAdded();
      if (dateAddedSong1 < dateAddedSong2) {
        return 1;
      } else if (dateAddedSong1 > dateAddedSong2) {
        return -1;
      } else {
        return 0;
      }
    }
  }
}