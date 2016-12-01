package com.kingbull.musicplayer.di;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.MediaStat;
import com.kingbull.musicplayer.domain.storage.LastPlayedPlayList;
import com.kingbull.musicplayer.domain.storage.MostPlayedPlayList;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import com.kingbull.musicplayer.domain.storage.SqlPlayList;
import com.kingbull.musicplayer.player.CallReceiver;
import com.kingbull.musicplayer.player.MusicService;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.main.categories.all.AllSongsPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.PlayListsModel;
import com.kingbull.musicplayer.ui.music.MusicPlayerFragment;
import com.kingbull.musicplayer.ui.music.MusicPlayerPresenter;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingAdapter;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingPresenter;
import com.kingbull.musicplayer.ui.songlist.SongsAdapter;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, StorageModule.class })
public interface AppComponent {
  // injection targets here

  void inject(MusicService service);

  void inject(CallReceiver receiver);

  void inject(MediaStat.Smart mediaStat);

  void inject(com.kingbull.musicplayer.ui.main.categories.all.SongsAdapter adapter);

  void inject(NowPlayingAdapter adapter);

  void inject(SqlMusic music);

  void inject(SqlPlayList playList);

  void inject(AddToPlayListDialogFragment fragment);

  void inject(PlayListsModel playListsModel);

  void inject(LastPlayedPlayList playList);

  void inject(MostPlayedPlayList playList);

  void inject(AllSongsPresenter presenter);

  void inject(NowPlayingPresenter presenter);

  void inject(MusicPlayerFragment fragment);

  void inject(MusicPlayerPresenter presenter);

  void inject(SongsAdapter adapter);

  MusicPlayerApp app();
}