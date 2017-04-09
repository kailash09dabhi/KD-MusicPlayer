package com.kingbull.musicplayer.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
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
    private final static String[] projections = new String[] {
        MediaStore.Audio.Playlists.Members.DATA, // the real path
        MediaStore.Audio.Playlists.Members.TITLE, MediaStore.Audio.Playlists.Members.DISPLAY_NAME,
        MediaStore.Audio.Playlists.Members.MIME_TYPE, MediaStore.Audio.Playlists.Members.ARTIST,
        MediaStore.Audio.Playlists.Members.ALBUM, MediaStore.Audio.Playlists.Members.ALBUM_ID,
        MediaStore.Audio.Playlists.Members.IS_RINGTONE, MediaStore.Audio.Playlists.Members.IS_MUSIC,
        MediaStore.Audio.Playlists.Members.IS_NOTIFICATION, MediaStore.Audio.Playlists.Members.SIZE,
        MediaStore.Audio.Playlists.Members.AUDIO_ID, MediaStore.Audio.Playlists.Members.DURATION,
        MediaStore.Audio.Playlists.Members.DATE_ADDED, MediaStore.Audio.Playlists.Members.YEAR
    };
    private final long playlistId;
    private final String name;

    public Smart(Cursor cursor) {
      playlistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID));
      name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME));
    }

    public Smart(Long playlistId, String name) {
      this.playlistId = playlistId;
      this.name = name;
    }

    protected Smart(Parcel in) {
      playlistId = in.readLong();
      name = in.readString();
    }

    public PlayList.Smart rename(String name) {
      ContentValues values = new ContentValues(1);
      values.put(MediaStore.Audio.Playlists.NAME, name);
      MusicPlayerApp.instance()
          .getContentResolver()
          .update(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values, "_id=" + playlistId,
              null);
      return new PlayList.Smart(playlistId, name);
    }

    @Override public String name() {
      return name;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeLong(playlistId);
      dest.writeString(name);
    }

    @Override public List<Music> musicList() {
      Cursor cursor = MusicPlayerApp.instance()
          .getContentResolver()
          .query(MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
              projections, MediaStore.Audio.Playlists.Members.DURATION + ">=" + "?", new String[] {
                  String.valueOf(playlistId)
              }, "");
      List<Music> musicList = new ArrayList<>();
      if (cursor != null) {
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
          do {
            SqlMusic song = new SqlMusic(new Media.PlaylistMember(cursor));
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

    public void addAll(List<? extends Music> musicList) {
      String[] cols = new String[] { "count(*)" };
      Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
      Cursor cursor =
          MusicPlayerApp.instance().getContentResolver().query(uri, cols, null, null, null);
      if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
        final int base = cursor.getInt(0);
        int size = musicList.size();
        if (size > 0) {
          //ContentValues[] contentValues = new ContentValues[size];
          for (int i = 0; i < size; i++) {
            Music music = musicList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,
                Long.valueOf(base + music.media().mediaId()));
            contentValues.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, music.media().mediaId());
            MusicPlayerApp.instance().getContentResolver().insert(uri, contentValues);
          }
          //MusicPlayerApp.newInstance().getContentResolver().bulkInsert(uri, contentValues);
        }
        cursor.close();
      }
    }

    public void remove(Music music) {
      String[] cols = new String[] { "count(*)" };
      Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
      Cursor cursor;
      cursor = MusicPlayerApp.instance().getContentResolver().query(uri, cols, null, null, null);
      if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
        MusicPlayerApp.instance()
            .getContentResolver()
            .delete(uri,
                MediaStore.Audio.Playlists.Members.AUDIO_ID + " = " + music.media().mediaId(),
                null);
        cursor.close();
      }
    }

    public void delete() {
      String where = MediaStore.Audio.Playlists._ID + "=?";
      String[] selectionArgs = { String.valueOf(playlistId) };
      MusicPlayerApp.instance()
          .getContentResolver()
          .delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, where, selectionArgs);
    }

    @Override public int hashCode() {
      int result = (int) (playlistId ^ (playlistId >>> 32));
      result = 31 * result + name.hashCode();
      return result;
    }

    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Smart smart = (Smart) o;
      if (playlistId != smart.playlistId) return false;
      return name.equals(smart.name);
    }
  }
}
