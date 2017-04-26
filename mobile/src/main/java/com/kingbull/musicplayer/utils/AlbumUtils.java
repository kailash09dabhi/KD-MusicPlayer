package com.kingbull.musicplayer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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

  public static Bitmap circularBitmap(Bitmap bitmap) {
    int size = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
    Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(output);
    final int color = 0xff424242;
    final Paint paint = new Paint();
    final Rect rect = new Rect(0, 0, size, size);
    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
    canvas.drawCircle(size / 2, size / 2, size / 2, paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);
    //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
    //return _bmp;
    return output;
  }
}
