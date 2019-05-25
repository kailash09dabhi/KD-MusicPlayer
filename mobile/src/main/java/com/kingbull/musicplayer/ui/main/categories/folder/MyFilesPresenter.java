package com.kingbull.musicplayer.ui.main.categories.folder;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import com.kingbull.musicplayer.domain.FileMusicMap;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.DefaultDisposableObserver;
import com.kingbull.musicplayer.ui.base.Presenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/10/2016.
 */
public final class MyFilesPresenter extends Presenter<MyFiles.View> implements MyFiles.Presenter {
  @Inject Player player;
  @Inject FileMusicMap fileMusicMap;
  @Inject MyFilesModel model;
  private final Observable<List<File>> currentFileListObservable =
      Observable.fromCallable(new Callable<List<File>>() {
        @Override public List<File> call() throws Exception {
          return model.filesOfCurrentFolder();
        }
      })
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnNext(new Consumer<List<File>>() {
            @Override public void accept(@io.reactivex.annotations.NonNull List<File> files) {
              view().showFiles(files);
            }
          });

  @Override public void takeView(@NonNull MyFiles.View view) {
    super.takeView(view);
    compositeDisposable = new CompositeDisposable();
    view().updateFolder(model.currentFolder());
    currentFileListObservable.subscribe(new DefaultDisposableObserver<List<File>>());
  }

  private Pair<File, Integer> lastClickedFolderAndItsIndexPair;

  @Override public void onFolderClick(final Pair<File, Integer> pairOfFolderAndItsIndex) {
    lastClickedFolderAndItsIndexPair = pairOfFolderAndItsIndex;
    final File folder = pairOfFolderAndItsIndex.first;
    model.currentFolder(folder);
    view().showProgressOnFolder(pairOfFolderAndItsIndex);
    Observable.fromCallable(new Callable<List<File>>() {
      @Override public List<File> call() throws Exception {
        return model.filesOfCurrentFolder();
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(
        new Consumer<List<File>>() {
          @Override public void accept(@io.reactivex.annotations.NonNull List<File> files) {
            if (lastClickedFolderAndItsIndexPair == pairOfFolderAndItsIndex) {
              view().showFiles(files);
              view().updateFolder(folder);
              view().hideProgressOnFolder(pairOfFolderAndItsIndex);
            }
          }
        }).subscribe();
  }

  @Override public void onBackPressed() {
    if (model.isReachedToTopDirectory()) {
      view().close();
    } else {
      model.currentFolder(model.currentFolder().getParentFile());
      currentFileListObservable.subscribe(new DefaultDisposableObserver<List<File>>());
      view().updateFolder(model.currentFolder());
    }
  }

  @Override public void onMusicClick(File musicFile) {
    player.addToNowPlaylist(model.musicListFromDirectory(musicFile.getParentFile()));
    player.play(fileMusicMap.music(musicFile));
    view().showMusicPlayer();
  }

  @Override public void onPaletteOrThemeEvent() {
    view().refresh();
  }
}
