package com.kingbull.musicplayer.domain.storage;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Music;
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
  private Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
  private String[] PROJECTIONS = {
      MediaStore.Audio.Media.DATA, // the real path
      MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.IS_RINGTONE, MediaStore.Audio.Media.IS_MUSIC,
      MediaStore.Audio.Media.IS_NOTIFICATION, MediaStore.Audio.Media.SIZE,
      MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.YEAR
  };

  public RecentlyAddedPlayList() {
  }

  protected RecentlyAddedPlayList(Parcel in) {
    MEDIA_URI = in.readParcelable(Uri.class.getClassLoader());
    PROJECTIONS = in.createStringArray();
  }

  @Override public String name() {
    return "Recently Added";
  }

  @Override public List<Music> musicList() {
    Cursor cursor = MusicPlayerApp.instance()
        .getContentResolver()
        .query(MEDIA_URI, PROJECTIONS, null, null, null);
    List<Music> itemList = new ArrayList<>();
    if (cursor != null) {
      if (cursor.getCount() > 0 && cursor.moveToFirst()) {
        do {
          SqlMusic song = new SqlMusic(new MediaCursor(cursor));
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
    dest.writeParcelable(MEDIA_URI, flags);
    dest.writeStringArray(PROJECTIONS);
  }

  final class RecentlyAddedComparator implements Comparator<Music> {

    @Override public int compare(Music song1, Music song2) {
      long dateAddedSong1 = song1.dateAdded();
      long dateAddedSong2 = song2.dateAdded();
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