package com.kingbull.musicplayer.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.sqlite.CurrentDateTime;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlTableRow;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaStatTable;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

public interface MediaStat extends SqlTableRow {

  boolean isFavorite();

  long mediaId();

  void addToPlaylist(long playListId);

  void saveLastPlayed();

  void toggleFavourite();

  class Smart implements MediaStat, Parcelable, SqlTableRow {
    public static final Creator<Smart> CREATOR = new Creator<Smart>() {
      @Override public Smart createFromParcel(Parcel in) {
        return new Smart(in);
      }

      @Override public Smart[] newArray(int size) {
        return new Smart[size];
      }
    };
    private final long lastTimePlayed;
    private final long mediaId;
    @Inject SQLiteDatabase sqliteDatabase;
    private boolean isFavourite;
    private long numberOfTimesPlayed;
    private String playlistIds;

    public Smart(Cursor cursor) {
      mediaId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStatTable.Columns.MEDIA_ID));
      isFavourite =
          cursor.getInt(cursor.getColumnIndexOrThrow(MediaStatTable.Columns.FAVORITE)) == 1;
      numberOfTimesPlayed = cursor.getLong(
          cursor.getColumnIndexOrThrow(MediaStatTable.Columns.NUMBER_OF_TIMES_PLAYED));
      playlistIds =
          cursor.getString(cursor.getColumnIndexOrThrow(MediaStatTable.Columns.PLAYLIST_IDS));
      lastTimePlayed =
          cursor.getLong(cursor.getColumnIndexOrThrow(MediaStatTable.Columns.LAST_TIME_PLAYED));
      MusicPlayerApp.instance().component().inject(this);
    }

    protected Smart(Parcel in) {
      isFavourite = in.readByte() != 0;
      numberOfTimesPlayed = in.readLong();
      playlistIds = in.readString();
      lastTimePlayed = in.readLong();
      mediaId = in.readLong();
      MusicPlayerApp.instance().component().inject(this);
    }

    public Smart(long mediaId) {
      this.mediaId = mediaId;
      this.isFavourite = false;
      this.playlistIds = "";
      this.numberOfTimesPlayed = 0;
      this.lastTimePlayed = 0;
      MusicPlayerApp.instance().component().inject(this);
    }

    @Override public boolean isFavorite() {
      return isFavourite;
    }

    @Override public long mediaId() {
      return mediaId;
    }

    @Override public void addToPlaylist(long playlistId) {
      this.playlistIds = this.playlistIds + "(" + playlistId + ")";
      save();
    }

    @Override public void saveLastPlayed() {
      numberOfTimesPlayed++;
      save();
    }

    @Override public void toggleFavourite() {
      this.isFavourite = !isFavourite;
      save();
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeByte((byte) (isFavourite ? 1 : 0));
      dest.writeLong(numberOfTimesPlayed);
      dest.writeString(playlistIds);
      dest.writeLong(lastTimePlayed);
      dest.writeLong(mediaId);
    }

    @Override public long save() {
      ContentValues values = new ContentValues();
      values.put(MediaStatTable.Columns.MEDIA_ID, mediaId);
      values.put(MediaStatTable.Columns.FAVORITE, isFavourite ? 1 : 0);
      values.put(MediaStatTable.Columns.PLAYLIST_IDS, playlistIds);
      values.put(MediaStatTable.Columns.LAST_TIME_PLAYED, new CurrentDateTime().toString());
      values.put(MediaStatTable.Columns.NUMBER_OF_TIMES_PLAYED, numberOfTimesPlayed);
      values.put(MediaStatTable.Columns.UPDATED_AT, new CurrentDateTime().toString());
      return sqliteDatabase.insertWithOnConflict(MediaStatTable.NAME, null, values,
          SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override public boolean delete() {
      return sqliteDatabase.delete(MediaStatTable.NAME, MediaStatTable.Columns.MEDIA_ID + "=" +
          mediaId, null) > 0;
    }
  }
}
