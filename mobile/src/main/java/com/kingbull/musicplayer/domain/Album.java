package com.kingbull.musicplayer.domain;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

public interface Album {

  Album NONE = new Album() {

    @Override public long albumId() {
      return 0;
    }

    @Override public String name() {
      return "Album";
    }

    @Override public String albumArt() {
      return "";
    }
  };

  long albumId();

  String name();

  String albumArt();

  class Smart implements Album, Parcelable {

    public static final Creator<Smart> CREATOR = new Creator<Smart>() {
      @Override public Smart createFromParcel(Parcel in) {
        return new Smart(in);
      }

      @Override public Smart[] newArray(int size) {
        return new Smart[size];
      }
    };
    private final int _id;
    private final String name;
    private final String albumArt;

    public Smart(Cursor cursor) {
      this._id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
      this.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
      this.albumArt =
          cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
    }

    public Smart(String name) {
      this._id = -1;
      this.name = name;
      this.albumArt = "";
    }

    protected Smart(Parcel in) {
      _id = in.readInt();
      name = in.readString();
      albumArt = in.readString();
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(_id);
      dest.writeString(name);
      dest.writeString(albumArt);
    }

    @Override public long albumId() {
      return _id;
    }

    @Override public String name() {
      return name;
    }

    @Override public String albumArt() {
      return albumArt;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Smart smart = (Smart) o;
      return name != null ? name.equals(smart.name) : smart.name == null;
    }

    @Override public int hashCode() {
      return name != null ? name.hashCode() : 0;
    }
  }
}
