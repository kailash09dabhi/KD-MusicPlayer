package com.kingbull.musicplayer.ui.main.songgroup.folder;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */

public final class Folder {
  private static final HashMap<File, List<File>> hashMap = new HashMap<>();

  List<File> audioFiles(File folder) {
    List<File> filesOfFolder;
    if (hashMap.containsKey(folder)) {
      filesOfFolder = hashMap.get(folder);
    } else {
      filesOfFolder = Arrays.asList(folder.listFiles(new AudioFileFilter()));
      hashMap.put(folder, filesOfFolder);
    }
    return filesOfFolder;
  }


}
