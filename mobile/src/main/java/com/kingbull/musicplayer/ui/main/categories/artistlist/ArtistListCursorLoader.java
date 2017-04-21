package com.kingbull.musicplayer.ui.main.categories.artistlist;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * @author Kailash Dabhi
 * @date 11/9/2016.
 */
final class ArtistListCursorLoader extends CursorLoader {
  private static final Uri MEDIA_URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
  private static final String ORDER_BY = MediaStore.Audio.Artists.ARTIST_KEY;
  private static String[] PROJECTIONS = {
      MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST,
      MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS
  };

  public ArtistListCursorLoader(Context context) {
    super(context, MEDIA_URI, PROJECTIONS, null, null, ORDER_BY);
  }
}
