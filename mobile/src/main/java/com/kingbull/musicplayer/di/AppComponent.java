package com.kingbull.musicplayer.di;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.player.PlaybackService;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, StorageModule.class })
public interface AppComponent {
  // injection targets here

  void inject(PlaybackService service);

  MusicPlayerApp app();
}