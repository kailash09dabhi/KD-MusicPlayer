package com.kingbull.musicplayer.ui.main.categories.folder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

public final class Directory {

  private static final AudioFileFilter audioFileFilter = new AudioFileFilter();

  private final File file;

  Directory(File file) {
    if (!file.isDirectory()) throw new RuntimeException("file is not directory!");
    this.file = file;
  }

  /*It will give total files in this folder by checking in subfolders too */
  public List<File> totalFiles() {
    List<File> totalFiles = new ArrayList<>();
    totalFiles.addAll(files());
    List<Directory> directories = directories();
    for (Directory directory : directories) {
      totalFiles.addAll(directory.files());
    }
    return totalFiles;
  }

  public List<File> files() {
    List<File> onlyFiles = new ArrayList<>();
    List<File> files = Arrays.asList(file.listFiles(audioFileFilter));
    for (File file : files) {
      if (!file.isDirectory()) onlyFiles.add(file);
    }
    return onlyFiles;
  }

  public List<Directory> directories() {
    List<Directory> onlyDirectories = new ArrayList<>();
    List<File> files = Arrays.asList(file.listFiles(audioFileFilter));
    for (File file : files) {
      if (file.isDirectory()) onlyDirectories.add(new Directory(file));
    }
    return onlyDirectories;
  }

  public List<File> directoriesAsFiles() {
    List<File> onlyDirectories = new ArrayList<>();
    List<File> files = Arrays.asList(file.listFiles(audioFileFilter));
    for (File file : files) {
      if (file.isDirectory()) onlyDirectories.add(file);
    }
    return onlyDirectories;
  }
}
