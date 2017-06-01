package com.kingbull.musicplayer.domain.storage.sqlite;

import android.os.Parcel;
import android.os.Parcelable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.domain.MediaStat;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaStatTable;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaTable;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */
public final class SqlMusic implements com.kingbull.musicplayer.domain.Music, Parcelable {
  public static final Parcelable.Creator<SqlMusic> CREATOR = new Parcelable.Creator<SqlMusic>() {
    @Override public SqlMusic createFromParcel(Parcel source) {
      return new SqlMusic(source);
    }

    @Override public SqlMusic[] newArray(int size) {
      return new SqlMusic[size];
    }
  };
  private final Media media;
  private final MediaStat mediaStat;
  @Inject MediaStatTable mediaStatTable;
  @Inject MediaTable mediaTable;

  public SqlMusic(Media media, MediaStat mediaStat) {
    MusicPlayerApp.instance().component().inject(this);
    this.media = media;
    this.mediaStat = mediaStat;
  }

  public SqlMusic(MediaStat mediaStat) {
    MusicPlayerApp.instance().component().inject(this);
    this.mediaStat = mediaStat;
    this.media = mediaTable.mediaById(mediaStat.mediaId());
  }

  public SqlMusic(Media media) {
    MusicPlayerApp.instance().component().inject(this);
    this.media = media;
    this.mediaStat = mediaStatTable.mediaStatById(media.mediaId());
  }

  protected SqlMusic(Parcel in) {
    MusicPlayerApp.instance().component().inject(this);
    media = in.readParcelable(Media.class.getClassLoader());
    mediaStat = in.readParcelable(MediaStat.class.getClassLoader());
  }

  @Override public String toString() {
    return "SqlMusic{" + "media=" + media.toString() + ", mediaStat=" + mediaStat.toString() + '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable((Parcelable) media, flags);
    dest.writeParcelable((Parcelable) mediaStat, flags);
  }

  @Override public Media media() {
    return media;
  }

  @Override public MediaStat mediaStat() {
    return mediaStat;
  }
}
