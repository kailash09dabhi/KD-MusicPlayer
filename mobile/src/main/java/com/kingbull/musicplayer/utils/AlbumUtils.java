package com.kingbull.musicplayer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import com.kingbull.musicplayer.domain.Music;
import java.io.File;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/14/16
 * Time: 8:42 PM
 * Desc: BitmapUtils
 * TODO To be optimized
 */
public class AlbumUtils {
  private static final String TAG = "AlbumUtils";

  public static Bitmap parseAlbum(Music song) {
    return parseAlbum(new File(song.media().path()));
  }

  private static Bitmap parseAlbum(File file) {
    MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
    try {
      metadataRetriever.setDataSource(file.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      Log.e(TAG, "parseAlbum: ", e);
    } catch (IllegalStateException e) {
      Log.e(TAG, "parseAlbum: ", e);
    } catch (RuntimeException e) {
      Log.e(TAG, "parseAlbum: ", e);
    }
    byte[] albumData = metadataRetriever.getEmbeddedPicture();
    if (albumData != null) {
      return BitmapFactory.decodeByteArray(albumData, 0, albumData.length);
    }
    metadataRetriever.release();
    return null;
  }
}
