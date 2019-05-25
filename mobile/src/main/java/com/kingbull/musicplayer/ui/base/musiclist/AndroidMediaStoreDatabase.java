package com.kingbull.musicplayer.ui.base.musiclist;

import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.kingbull.musicplayer.BuildConfig;
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
        BuildConfig.APPLICATION_ID + ".provider",
        new File(mediaPath)
    );
    Intent intent = new Intent(Intent.ACTION_DELETE, uri);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    app.sendBroadcast(intent);
  }
}
