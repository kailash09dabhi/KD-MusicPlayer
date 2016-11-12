package com.kingbull.musicplayer.ui.main.songgroup.folder;

import android.os.Environment;
import java.io.File;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */

public final class MyFilesModel implements MyFiles.Model {
  private final File topFolder = Environment.getExternalStorageDirectory();
  Folder folder = new Folder();
  private File currentFolder = topFolder;

  @Override public File currentFolder() {
    return currentFolder;
  }

  @Override public List<File> filesOfCurrentFolder() {
    return folder.audioFiles(currentFolder);
  }

  @Override public void currentFolder(File file) {
    currentFolder = file;
  }

  @Override public boolean isReachedToTopDirectory() {
    return currentFolder.equals(topFolder);
  }
}
