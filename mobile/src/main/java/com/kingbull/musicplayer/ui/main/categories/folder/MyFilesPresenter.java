package com.kingbull.musicplayer.ui.main.categories.folder;

import android.support.annotation.NonNull;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.ui.base.Presenter;
import java.io.File;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */

public final class MyFilesPresenter extends Presenter<MyFiles.View> implements MyFiles.Presenter {

  MyFiles.Model model = new MyFilesModel();
  private CompositeSubscription compositeSubscription;

  @Override public void takeView(@NonNull MyFiles.View view) {
    super.takeView(view);
    compositeSubscription = new CompositeSubscription();
    view().updateFolder(model.currentFolder());
    view().showFiles(model.filesOfCurrentFolder());
  }

  @Override public void onEitherFolderOrSongClick(File file) {
    if (file.isDirectory()) {
      model.currentFolder(file);
      view().showFiles(model.filesOfCurrentFolder());
      view().updateFolder(file);
    } else {
      // TODO: 11/12/2016  go to  music fragment and play song

    }
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

  @Override public void onSongClick(Song song) {
    view().showMusicPlayer(song);
  }
}
