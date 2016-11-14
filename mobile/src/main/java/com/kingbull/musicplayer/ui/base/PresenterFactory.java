package com.kingbull.musicplayer.ui.base;

import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesPresenter;
import com.kingbull.musicplayer.ui.songlist.SongListPresenter;

/**
 * Creates a Presenter object.
 *
 * @param <T> presenter type
 */
public interface PresenterFactory<T extends Presenter> {
  T create();

  class SongList implements PresenterFactory<SongListPresenter> {

    @Override public SongListPresenter create() {
      return new SongListPresenter();
    }
  }

  class MyFiles implements PresenterFactory<MyFilesPresenter> {

    @Override public MyFilesPresenter create() {
      return new MyFilesPresenter();
    }
  }
}