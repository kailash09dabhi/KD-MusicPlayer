package com.kingbull.musicplayer.ui.main.categories.folder;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.MediaCursor;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */

public final class SongFile {

  private static final HashMap<File, Music> hashMap = new HashMap<>();
  private final File songFile;

  public SongFile(File songFile) {
    this.songFile = songFile;
  }

  Music song(Context context) throws IOException {
    Music song;
    if (hashMap.containsKey(songFile)) {
      song = hashMap.get(songFile);
    } else {
   Cursor cursor =  context.getContentResolver()
          .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] {
              MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
              MediaStore.Audio.Media.TRACK, MediaStore.Audio.Media.TITLE,
              MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA,
              MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.YEAR,
              MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media._ID,
              MediaStore.Audio.Media.DATE_ADDED
          }, MediaStore.Audio.Media.DATA + " = ?", new String[] {
              songFile.getCanonicalPath()
          }, "");
      cursor.moveToFirst();
      song = new  SqlMusic(new MediaCursor(cursor));
      hashMap.put(songFile, song);
    }
    return song;
  }
  //MediaMetadataRetriever mmr = new MediaMetadataRetriever();
  //Song song;
  //if (hashMap.containsKey(songFile)) {
  //  song = hashMap.get(songFile);
  //} else {
  //  mmr.setDataSource(songFile.getAbsolutePath());
  //  String artistName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
  //  String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
  //  song.
  //      hashMap.put(folder, song);
  //}
  //return song;
}
