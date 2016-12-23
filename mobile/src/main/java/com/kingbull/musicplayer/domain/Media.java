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

  Media NONE = new Media() {
    @Override public long mediaId() {
      return 0;
    }

    @Override public String title() {
      return "";
    }

    @Override public String artist() {
      return "";
    }

    @Override public String album() {
      return "";
    }

    @Override public long albumId() {
      return 0;
    }

    @Override public String path() {
      return "";
    }

    @Override public long duration() {
      return 0;
    }

    @Override public int size() {
      return 0;
    }

    @Override public long dateAdded() {
      return 0;
    }

    @Override public long year() {
      return 0;
    }
  };

  long mediaId();

  String title();

  String artist();

  String album();

  long albumId();

  String path();

  long duration();

  int size();

  long dateAdded();

  long year();

  class Smart implements Media, Parcelable {

    public static final Creator<Smart> CREATOR = new Creator<Smart>() {
      @Override public Smart createFromParcel(Parcel in) {
        return new Smart(in);
      }

      @Override public Smart[] newArray(int size) {
        return new Smart[size];
      }
    };
    private final long duration;
    private final int size;
    private final String title;
    private final long year;
    private final long mediaId;
    private final String artist;
    private final String album;
    private final String path;
    private final long dateAdded;
    private final long albumId;

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
      albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
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
      albumId = in.readLong();
    }

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

    @Override public long albumId() {
      return albumId;
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
      dest.writeLong(albumId);
    }
  }

  class PlaylistMember implements Media, Parcelable {

    public static final Creator<Smart> CREATOR = new Creator<Smart>() {
      @Override public Smart createFromParcel(Parcel in) {
        return new Smart(in);
      }

      @Override public Smart[] newArray(int size) {
        return new Smart[size];
      }
    };
    private final long duration;
    private final int size;
    private final String title;
    private final long year;
    private final long mediaId;
    private final String artist;
    private final String album;
    private final String path;
    private final long dateAdded;
    private final long albumId;

    public PlaylistMember(Cursor cursor) {
      mediaId =
          cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.AUDIO_ID));
      title =
          cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.TITLE));
      artist =
          cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ARTIST));
      album =
          cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM));
      path =
          cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.DATA));
      duration =
          cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.DURATION));
      size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.SIZE));
      dateAdded = new File(path()).lastModified();
      year = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.YEAR));
      albumId =
          cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_ID));
    }

    protected PlaylistMember(Parcel in) {
      duration = in.readLong();
      size = in.readInt();
      title = in.readString();
      year = in.readLong();
      mediaId = in.readLong();
      artist = in.readString();
      album = in.readString();
      path = in.readString();
      dateAdded = in.readLong();
      albumId = in.readLong();
    }

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

    @Override public long albumId() {
      return albumId;
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
      dest.writeLong(albumId);
    }
  }
}
