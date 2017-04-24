package com.kingbull.musicplayer.ui.main.categories.folder;

import android.support.annotation.NonNull;
import com.kingbull.musicplayer.domain.FileMusicMap;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.Presenter;
import io.reactivex.disposables.CompositeDisposable;
import java.io.File;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */
public final class MyFilesPresenter extends Presenter<MyFiles.View> implements MyFiles.Presenter {
  @Inject Player player;
  @Inject FileMusicMap fileMusicMap;
  @Inject MyFilesModel model;

  @Override public void takeView(@NonNull MyFiles.View view) {
    super.takeView(view);
    compositeDisposable = new CompositeDisposable();
    view().updateFolder(model.currentFolder());
    view().showFiles(model.filesOfCurrentFolder());
  }

  @Override public void onFolderClick(File file) {
    model.currentFolder(file);
    view().showFiles(model.filesOfCurrentFolder());
    view().updateFolder(file);
  }

  @Override public void onBackPressed() {
    if (model.isReachedToTopDirectory()) {
      view().close();
    } else {
      model.currentFolder(model.currentFolder().getParentFile());
      view().showFiles(model.filesOfCurrentFolder());
      view().updateFolder(model.currentFolder());
    }
  }

  @Override public void onMusicClick(File musicFile) {
    player.addToNowPlaylist(model.musicListFromDirectory(musicFile.getParentFile()));
    player.play(fileMusicMap.music(musicFile));
    view().showMusicPlayer();
  }
}
