package com.kingbull.musicplayer.ui.base;

import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed.LastPlayedPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.mostplayed.MostPlayedPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.recentlyadded.RecentlyAddedPresenter;
import com.kingbull.musicplayer.ui.music.MusicPlayerPresenter;
import com.kingbull.musicplayer.ui.songlist.SongListPresenter;

/**
 * Creates a Presenter object.
 *
 * @param <T> presenter type
 */
public interface PresenterFactory<T extends Mvp.Presenter> {
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

  class RecentlyAdded implements
      PresenterFactory<com.kingbull.musicplayer.ui.main.categories.playlists.recentlyadded.RecentlyAdded.Presenter> {

    @Override
    public com.kingbull.musicplayer.ui.main.categories.playlists.recentlyadded.RecentlyAdded.Presenter create() {
      return new RecentlyAddedPresenter();
    }
  }

  class MusicPlayer
      implements PresenterFactory<com.kingbull.musicplayer.ui.music.MusicPlayer.Presenter> {

    @Override public com.kingbull.musicplayer.ui.music.MusicPlayer.Presenter create() {
      return new MusicPlayerPresenter();
    }
  }

  class LastPlayed implements
      PresenterFactory<com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed.LastPlayed.Presenter> {

    @Override
    public com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed.LastPlayed.Presenter create() {
      return new LastPlayedPresenter();
    }
  }

  class MostPlayed implements
      PresenterFactory<com.kingbull.musicplayer.ui.main.categories.playlists.mostplayed.MostPlayed.Presenter> {

    @Override
    public com.kingbull.musicplayer.ui.main.categories.playlists.mostplayed.MostPlayed.Presenter create() {
      return new MostPlayedPresenter();
    }
  }
}