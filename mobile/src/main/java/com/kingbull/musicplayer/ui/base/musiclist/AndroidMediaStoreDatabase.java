package com.kingbull.musicplayer.ui.base.musiclist;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import com.kingbull.musicplayer.MusicPlayerApp;
import java.io.File;

/**
 * @author Kailash Dabhi
 * @date 4/17/2017
 */
public final class AndroidMediaStoreDatabase {
  public void deleteAndBroadcastDeletion(String mediaPath) {
    MusicPlayerApp app = MusicPlayerApp.instance();
    app.getContentResolver()
        .delete(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            android.provider.MediaStore.MediaColumns.DATA + "=?", new String[]{mediaPath});
    Uri uri = FileProvider.getUriForFile(app,
        app.getApplicationContext().getPackageName() + ".GenericFileProvider",
        new File(mediaPath)
    );
    app.sendBroadcast(new Intent(Intent.ACTION_DELETE, uri));
  }
}
