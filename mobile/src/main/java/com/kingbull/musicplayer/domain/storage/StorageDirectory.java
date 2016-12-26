package com.kingbull.musicplayer.domain.storage;

import android.os.Environment;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import java.io.File;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 12/27/2016.
 */

public final class StorageDirectory {
  private final String directoryName;

  @Inject public StorageDirectory(String directoryName) {
    this.directoryName = directoryName;
  }

  public File asFile() {
    File file = new File(Environment.getExternalStorageDirectory(),
        MusicPlayerApp.instance().getString(R.string.app_name));
    if (!file.mkdirs()) {
      // Log.e("SignaturePad", "Directory not created");
    }
    file = new File(file, directoryName);
    if (!file.mkdirs()) {
      // Log.e("SignaturePad", "Directory not created");
    }
    return file;
  }
}
