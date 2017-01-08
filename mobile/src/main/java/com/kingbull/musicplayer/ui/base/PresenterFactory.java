package com.kingbull.musicplayer.ui.base;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.ui.coverarts.CoverArts;
import com.kingbull.musicplayer.ui.coverarts.CoverArtsPresenter;
import com.kingbull.musicplayer.ui.equalizer.EqualizerPresenter;
import com.kingbull.musicplayer.ui.main.categories.albumlist.album.AlbumPresenter;
import com.kingbull.musicplayer.ui.main.categories.all.AllSongsPresenter;
import com.kingbull.musicplayer.ui.main.categories.artistlist.artist.ArtistPresenter;
import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesPresenter;
import com.kingbull.musicplayer.ui.main.categories.genreslist.genre.GenresPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.PlayLists;
import com.kingbull.musicplayer.ui.main.categories.playlists.PlayListsPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.members.Members;
import com.kingbull.musicplayer.ui.main.categories.playlists.members.MembersPresenter;
import com.kingbull.musicplayer.ui.music.MusicPlayerPresenter;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingPresenter;
import com.kingbull.musicplayer.ui.settings.SettingsPresenter;
import com.kingbull.musicplayer.ui.statistics.StatisticsPresenter;

/**
 * Creates a Presenter object.
 *
 * @param <T> presenter type
 */
public interface PresenterFactory<T extends Mvp.Presenter> {
  T create();

  class SongList implements PresenterFactory<GenresPresenter> {

    @Override public GenresPresenter create() {
      return new GenresPresenter();
    }
  }

  class Statistics
      implements PresenterFactory<com.kingbull.musicplayer.ui.statistics.Statistics.Presenter> {

    @Override public com.kingbull.musicplayer.ui.statistics.Statistics.Presenter create() {
      return new StatisticsPresenter();
    }
  }

  class Album implements
      PresenterFactory<com.kingbull.musicplayer.ui.main.categories.albumlist.album.Album.Presenter> {

    @Override
    public com.kingbull.musicplayer.ui.main.categories.albumlist.album.Album.Presenter create() {
      return new AlbumPresenter();
    }
  }

  class Artist implements
      PresenterFactory<com.kingbull.musicplayer.ui.main.categories.artistlist.artist.Artist.Presenter> {

    @Override
    public com.kingbull.musicplayer.ui.main.categories.artistlist.artist.Artist.Presenter create() {
      return new ArtistPresenter();
    }
  }

  class CoverArt implements PresenterFactory<CoverArts.Presenter> {

    @Override public CoverArts.Presenter create() {
      return new CoverArtsPresenter();
    }
  }

  class MyFiles implements PresenterFactory<MyFilesPresenter> {

    @Override public MyFilesPresenter create() {
      MyFilesPresenter presenter = new MyFilesPresenter();
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

  class Equalizer
      implements PresenterFactory<com.kingbull.musicplayer.ui.equalizer.Equalizer.Presenter> {

    @Override public com.kingbull.musicplayer.ui.equalizer.Equalizer.Presenter create() {
      return new EqualizerPresenter();
    }
  }

  class Playlists implements PresenterFactory<PlayLists.Presenter> {

    @Override public PlayLists.Presenter create() {
      return new PlayListsPresenter();
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

  class Settings
      implements PresenterFactory<com.kingbull.musicplayer.ui.settings.Settings.Presenter> {

    @Override public com.kingbull.musicplayer.ui.settings.Settings.Presenter create() {
      return new SettingsPresenter();
    }
  }

  class MusicListOfPlaylist implements PresenterFactory<Members.Presenter> {

    @Override public Members.Presenter create() {
      return new MembersPresenter();
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