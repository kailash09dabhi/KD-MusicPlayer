package com.kingbull.musicplayer.ui.main.categories.folder;

import android.os.Environment;
import com.kingbull.musicplayer.domain.FileMusicMap;
import com.kingbull.musicplayer.domain.Music;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */

public final class MyFilesModel implements MyFiles.Model {
  private final File topFolder = Environment.getExternalStorageDirectory();
  @Inject FileMusicMap fileMusicMap;
  private File currentFolder = topFolder;

  @Inject MyFilesModel() {
  }

  @Override public File currentFolder() {
    return currentFolder;
  }

  @Override public List<File> filesOfCurrentFolder() {
    Directory directory = new Directory(currentFolder);
    List<File> files = directory.directoriesAsFiles();
    files.addAll(directory.files());
    return files;
  }

  @Override public void currentFolder(File file) {
    currentFolder = file;
  }

  @Override public boolean isReachedToTopDirectory() {
    return currentFolder.equals(topFolder);
  }

  @Override public List<Music> musicListFromDirectory(File directory) {
    File[] files = directory.listFiles(new AudioFileFilter());
    List<Music> musicList = new ArrayList<>();
    for (File file : files) {
      if (!file.isDirectory()) musicList.add(fileMusicMap.music(file));
    }
    return musicList;
  }
}
