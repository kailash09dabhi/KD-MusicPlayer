package com.kingbull.musicplayer.di;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import com.kingbull.musicplayer.player.PlaybackService;
import com.kingbull.musicplayer.ui.main.categories.playlists.lastplayed.LastPlayedModel;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, StorageModule.class })
public interface AppComponent {
  // injection targets here

  void inject(PlaybackService service);

  void inject(SqlMusic music);

  void inject(LastPlayedModel lastPlayedModel);

  MusicPlayerApp app();
}