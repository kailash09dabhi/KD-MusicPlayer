package com.kingbull.musicplayer.domain.storage;

import android.graphics.Bitmap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Kailash Dabhi
 * @date 12/27/2016.
 */
public final class ImageFile {
  private final File file;

  public ImageFile(File file) {
    this.file = file;
  }

  public void save(Bitmap bitmap) throws IOException {
    FileOutputStream out = new FileOutputStream(file);
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
    out.flush();
    out.close();
  }
}
