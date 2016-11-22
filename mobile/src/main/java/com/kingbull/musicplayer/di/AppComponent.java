package com.kingbull.musicplayer.di;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import com.kingbull.musicplayer.player.MusicService;
import com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed.LastPlayedModel;
import com.kingbull.musicplayer.ui.main.categories.playlists.mostplayed.MostPlayedModel;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, StorageModule.class })
public interface AppComponent {
  // injection targets here

  void inject(MusicService service);

  void inject(SqlMusic music);

  void inject(LastPlayedModel lastPlayedModel);

  void inject(MostPlayedModel mostPlayedModel);

  MusicPlayerApp app();
}