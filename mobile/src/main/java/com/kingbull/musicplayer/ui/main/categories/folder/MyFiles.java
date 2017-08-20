package com.kingbull.musicplayer.ui.main.categories.folder;

import android.support.v4.util.Pair;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.Mvp;
import java.io.File;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */
public interface MyFiles {
  interface View extends Mvp.View {
    void showFiles(List<File> songs);

    void updateFolder(File file);

    void close();

    void showMusicPlayer();

    void refresh();

    void showProgressOnFolder(android.support.v4.util.Pair<File, Integer> pairOfFolderAndItsIndex);

    void hideProgressOnFolder(android.support.v4.util.Pair<File, Integer> pairOfFolderAndItsIndex);
  }

  interface Model extends Mvp.Model {
    File currentFolder();

    List<File> filesOfCurrentFolder();

    void currentFolder(File file);

    boolean isReachedToTopDirectory();

    List<Music> musicListFromDirectory(File directory);
  }

  interface Presenter extends Mvp.Presenter<MyFiles.View> {
    void onFolderClick(Pair<File, Integer> pairOfFolderAndItsIndex);

    void onBackPressed();

    void onMusicClick(File musicFile);

    void onPaletteOrThemeEvent();
  }
}
