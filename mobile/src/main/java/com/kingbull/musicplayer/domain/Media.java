package com.kingbull.musicplayer.domain;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import java.io.File;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

public interface Media {

  long mediaId();

  String title();

  String artist();

  String album();

  String path();

  long duration();

  int size();

  long dateAdded();

  long year();

  class Smart implements Media ,Parcelable{

    private final long duration;
    private final int size;
    private final String title;
    private final long year;
    private final long mediaId;
    private final String artist;
    private final String album;
    private final String path;
    private final long dateAdded;

    public Smart(Cursor cursor) {
      mediaId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
      title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
      artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
      album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
      path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
      duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
      size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
      dateAdded = new File(path()).lastModified();
      year = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
    }

    protected Smart(Parcel in) {
      duration = in.readLong();
      size = in.readInt();
      title = in.readString();
      year = in.readLong();
      mediaId = in.readLong();
      artist = in.readString();
      album = in.readString();
      path = in.readString();
      dateAdded = in.readLong();
    }

    public static final Creator<Smart> CREATOR = new Creator<Smart>() {
      @Override public Smart createFromParcel(Parcel in) {
        return new Smart(in);
      }

      @Override public Smart[] newArray(int size) {
        return new Smart[size];
      }
    };

    @Override public long mediaId() {
      return mediaId;
    }

    @Override public String title() {
      return title;
    }

    @Override public String artist() {
      return artist;
    }

    @Override public String album() {
      return album;
    }

    @Override public String path() {
      return path;
    }

    @Override public long duration() {
      return duration;
    }

    @Override public int size() {
      return size;
    }

    @Override public long dateAdded() {
      return dateAdded;
    }

    @Override public long year() {
      return year;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeLong(duration);
      dest.writeInt(size);
      dest.writeString(title);
      dest.writeLong(year);
      dest.writeLong(mediaId);
      dest.writeString(artist);
      dest.writeString(album);
      dest.writeString(path);
      dest.writeLong(dateAdded);
    }
  }
}