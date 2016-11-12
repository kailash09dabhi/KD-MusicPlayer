package com.kingbull.musicplayer.ui.main.songgroup.folder;

import com.kingbull.musicplayer.domain.Song;
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

    void showMusicPlayer(Song song);
  }

  interface Model extends Mvp.Model {
    File currentFolder();

    List<File> filesOfCurrentFolder();


    void currentFolder(File file);

    boolean isReachedToTopDirectory();
  }

  interface Presenter extends Mvp.Presenter<MyFiles.View> {
    void onEitherFolderOrSongClick(File file);

    void onBackPressed();

    void onSongClick(Song song);
  }
}
