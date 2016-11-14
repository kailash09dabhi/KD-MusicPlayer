package com.kingbull.musicplayer.domain;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/2/16
 * Time: 4:01 PM
 * Desc: Genre
 */
//@Table("song")
public final class Song implements Parcelable {

  public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
    @Override public Song createFromParcel(Parcel source) {
      return new Song(source);
    }

    @Override public Song[] newArray(int size) {
      return new Song[size];
    }
  };
  //@PrimaryKey(AssignType.AUTO_INCREMENT)
  private int id;
  private String title;
  private String displayName;
  private String artist;
  private String album;
  //@Unique
  private String path;
  private int duration;
  private int size;
  private boolean favorite;

  public Song() {
    // Empty
  }

  public Song(Parcel in) {
    readFromParcel(in);
  }

  public Song(Cursor cursor) {
    title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
    String displayName =
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
    if (displayName.endsWith(".mp3")) {
      displayName = displayName.substring(0, displayName.length() - 4);
    }
    this.displayName = displayName;
    artist = (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
    album = (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
    path = (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
    duration = (cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
    size = (cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.title);
    dest.writeString(this.displayName);
    dest.writeString(this.artist);
    dest.writeString(this.album);
    dest.writeString(this.path);
    dest.writeInt(this.duration);
    dest.writeInt(this.size);
    dest.writeInt(this.favorite ? 1 : 0);
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Song song = (Song) o;
    if (id != song.id) return false;
    if (duration != song.duration) return false;
    if (size != song.size) return false;
    if (favorite != song.favorite) return false;
    if (title != null ? !title.equals(song.title) : song.title != null) return false;
    if (displayName != null ? !displayName.equals(song.displayName) : song.displayName != null) {
      return false;
    }
    if (artist != null ? !artist.equals(song.artist) : song.artist != null) return false;
    if (album != null ? !album.equals(song.album) : song.album != null) return false;
    return path != null ? path.equals(song.path) : song.path == null;
  }

  @Override public int hashCode() {
    int result = id;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
    result = 31 * result + (artist != null ? artist.hashCode() : 0);
    result = 31 * result + (album != null ? album.hashCode() : 0);
    result = 31 * result + (path != null ? path.hashCode() : 0);
    result = 31 * result + duration;
    result = 31 * result + size;
    result = 31 * result + (favorite ? 1 : 0);
    return result;
  }

  public void readFromParcel(Parcel in) {
    this.id = in.readInt();
    this.title = in.readString();
    this.displayName = in.readString();
    this.artist = in.readString();
    this.album = in.readString();
    this.path = in.readString();
    this.duration = in.readInt();
    this.size = in.readInt();
    this.favorite = in.readInt() == 1;
  }
}
