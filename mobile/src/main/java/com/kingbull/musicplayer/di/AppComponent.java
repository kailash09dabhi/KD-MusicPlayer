package com.kingbull.musicplayer.di;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import com.kingbull.musicplayer.player.MusicService;
import com.kingbull.musicplayer.ui.main.categories.all.AllSongsPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed.LastPlayedModel;
import com.kingbull.musicplayer.ui.main.categories.playlists.mostplayed.MostPlayedModel;
import com.kingbull.musicplayer.ui.main.categories.playlists.recentlyadded.RecentlyAdded;
import com.kingbull.musicplayer.ui.main.categories.playlists.recentlyadded.RecentlyAddedPresenter;
import com.kingbull.musicplayer.ui.music.MusicPlayerFragment;
import com.kingbull.musicplayer.ui.music.MusicPlayerPresenter;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingPresenter;
import com.kingbull.musicplayer.ui.songlist.SongsAdapter;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, StorageModule.class })
public interface AppComponent {
  // injection targets here

  void inject(MusicService service);

  void inject(com.kingbull.musicplayer.ui.main.categories.all.SongsAdapter adapter);

  void inject(SqlMusic music);

  void inject(LastPlayedModel lastPlayedModel);

  void inject(MostPlayedModel mostPlayedModel);

  void inject(RecentlyAdded RecentlyAdded);

  void inject(AllSongsPresenter presenter);

  void inject(RecentlyAddedPresenter presenter);

  void inject(NowPlayingPresenter presenter);

  void inject(MusicPlayerFragment fragment);

  void inject(MusicPlayerPresenter presenter);

  void inject(SongsAdapter adapter);

  MusicPlayerApp app();
}