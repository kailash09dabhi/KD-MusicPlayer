package com.kingbull.musicplayer.domain;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

public interface Artist {

  Artist NONE = new Artist() {

    @Override public long artistId() {
      return 0;
    }

    @Override public String name() {
      return "ArtistList";
    }
  };

  long artistId();

  String name();

  class Smart implements Artist, Parcelable {

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

    public Smart(Cursor cursor) {
      this._id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
      this.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
    }

    public Smart(String name) {
      this.name = name;
      this._id = -1;
    }

    protected Smart(Parcel in) {
      _id = in.readInt();
      name = in.readString();
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(_id);
      dest.writeString(name);
    }

    @Override public long artistId() {
      return _id;
    }

    @Override public String name() {
      return name;
    }
  }
}
