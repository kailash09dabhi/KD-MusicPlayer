package com.kingbull.musicplayer.domain;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */

public interface PlayList {
  PlayList NONE = new PlayList() {
    @Override public String name() {
      return "";
    }

    @Override public List<Music> musicList() {
      return Collections.emptyList();
    }
  };

  String name();

  List<Music> musicList();

  class Smart implements PlayList, Parcelable {
    public static final Creator<Smart> CREATOR = new Creator<Smart>() {
      @Override public Smart createFromParcel(Parcel in) {
        return new Smart(in);
      }

      @Override public Smart[] newArray(int size) {
        return new Smart[size];
      }
    };
    private final long playlistId;
    private final String name;

    public Smart(Cursor cursor) {
      playlistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID));
      name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME));
    }

    protected Smart(Parcel in) {
      playlistId = in.readLong();
      name = in.readString();
    }

    @Override public String name() {
      return name;
    }

    @Override public List<Music> musicList() {
      Cursor cursor = MusicPlayerApp.instance()
          .getContentResolver()
          .query(MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
              MediaTable.projections(), MediaStore.Audio.Playlists.Members.DURATION + ">=" + "?",
              new String[] {
                  String.valueOf(playlistId)
              }, "");
      List<Music> musicList = new ArrayList<>();
      if (cursor != null) {
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
          do {
            SqlMusic song = new SqlMusic(new Media.Smart(cursor));
            if (song.media() == Media.NONE) {
              song.mediaStat().delete();
            } else {
              musicList.add(song);
            }
          } while (cursor.moveToNext());
        }
        cursor.close();
      }
      return musicList;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeLong(playlistId);
      dest.writeString(name);
    }
  }
}
