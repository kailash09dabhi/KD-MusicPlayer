package com.kingbull.musicplayer.ui.base;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.ui.equalizer.EqualizerPresenter;
import com.kingbull.musicplayer.ui.main.categories.all.AllSongsPresenter;
import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed.LastPlayedPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.mostplayed.MostPlayedPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.recentlyadded.RecentlyAddedPresenter;
import com.kingbull.musicplayer.ui.music.MusicPlayerPresenter;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingPresenter;
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
      RecentlyAddedPresenter presenter = new RecentlyAddedPresenter();
      MusicPlayerApp.instance().component().inject(presenter);
      return presenter;
    }
  }

  class MusicPlayer
      implements PresenterFactory<com.kingbull.musicplayer.ui.music.MusicPlayer.Presenter> {

    @Override public com.kingbull.musicplayer.ui.music.MusicPlayer.Presenter create() {
      MusicPlayerPresenter presenter = new MusicPlayerPresenter();
      MusicPlayerApp.instance().component().inject(presenter);
      return presenter;
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

  class Equalizer
      implements PresenterFactory<com.kingbull.musicplayer.ui.equalizer.Equalizer.Presenter> {

    @Override public com.kingbull.musicplayer.ui.equalizer.Equalizer.Presenter create() {
      return new EqualizerPresenter();
    }
  }

  class NowPlaying
      implements PresenterFactory<com.kingbull.musicplayer.ui.nowplaying.NowPlaying.Presenter> {

    @Override public com.kingbull.musicplayer.ui.nowplaying.NowPlaying.Presenter create() {
      NowPlayingPresenter presenter = new NowPlayingPresenter();
      MusicPlayerApp.instance().component().inject(presenter);
      return presenter;
    }
  }

  class AllSongs implements
      PresenterFactory<com.kingbull.musicplayer.ui.main.categories.all.AllSongs.Presenter> {

    @Override public com.kingbull.musicplayer.ui.main.categories.all.AllSongs.Presenter create() {
      AllSongsPresenter presenter = new AllSongsPresenter();
      MusicPlayerApp.instance().component().inject(presenter);
      return presenter;
    }
  }
}