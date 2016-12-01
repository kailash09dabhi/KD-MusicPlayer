package com.kingbull.musicplayer.ui.main.categories.folder;

import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.io.File;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */

/*package*/ interface MyFiles {
  interface View extends Mvp.View {
    void showFiles(List<File> songs);

    void updateFolder(File file);

    void close();

    void showMusicPlayer();
  }

  interface Model extends Mvp.Model {
    File currentFolder();

    List<File> filesOfCurrentFolder();

    void currentFolder(File file);

    boolean isReachedToTopDirectory();

    List<Music> musicListFromDirectory(File directory);
  }

  interface Presenter extends Mvp.Presenter<MyFiles.View> {
    void onFolderClick(File directory);

    void onBackPressed();

    void onMusicClick(File musicFile);
  }
}
