package com.kingbull.musicplayer.di;

import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.MediaStat;
import com.kingbull.musicplayer.domain.storage.sqlite.FavouritesPlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.LastPlayedPlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.MostPlayedPlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlEqualizerPreset;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.player.CallReceiver;
import com.kingbull.musicplayer.player.MusicService;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.coverarts.CoverArtsFragment;
import com.kingbull.musicplayer.ui.equalizer.EqualizerFragment;
import com.kingbull.musicplayer.ui.equalizer.EqualizerModel;
import com.kingbull.musicplayer.ui.equalizer.preset.PresetDialogFragment;
import com.kingbull.musicplayer.ui.equalizer.reverb.PresetReverbDialogFragment;
import com.kingbull.musicplayer.ui.main.MusicCategoryFragment;
import com.kingbull.musicplayer.ui.main.categories.albumlist.AlbumListFragment;
import com.kingbull.musicplayer.ui.main.categories.albumlist.album.AlbumActivity;
import com.kingbull.musicplayer.ui.main.categories.albumlist.album.AlbumPresenter;
import com.kingbull.musicplayer.ui.main.categories.all.AllSongsFragment;
import com.kingbull.musicplayer.ui.main.categories.all.AllSongsPresenter;
import com.kingbull.musicplayer.ui.main.categories.artistlist.ArtistListFragment;
import com.kingbull.musicplayer.ui.main.categories.artistlist.artist.ArtistActivity;
import com.kingbull.musicplayer.ui.main.categories.artistlist.artist.ArtistPresenter;
import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesAdapter;
import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesFragment;
import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesPresenter;
import com.kingbull.musicplayer.ui.main.categories.genreslist.GenresListFragment;
import com.kingbull.musicplayer.ui.main.categories.genreslist.genre.GenreActivity;
import com.kingbull.musicplayer.ui.main.categories.genreslist.genre.GenresPresenter;
import com.kingbull.musicplayer.ui.main.categories.playlists.AllPlayListFragment;
import com.kingbull.musicplayer.ui.main.categories.playlists.AllPlaylistModel;
import com.kingbull.musicplayer.ui.main.categories.playlists.members.MembersFragment;
import com.kingbull.musicplayer.ui.main.categories.playlists.members.MembersRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.music.MusicPlayerFragment;
import com.kingbull.musicplayer.ui.music.MusicPlayerPresenter;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingAdapter;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingFragment;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingPresenter;
import com.kingbull.musicplayer.ui.settings.SettingsFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, StorageModule.class })
public interface AppComponent {
  void inject(MusicService service);

  void inject(SqlEqualizerPreset preset);

  void inject(SqlMusic music);

  void inject(MediaStat.Smart mediaStat);

  void inject(CallReceiver receiver);

  //adapter
  void inject(MembersRecyclerViewAdapter adapter);

  void inject(NowPlayingAdapter adapter);

  void inject(MyFilesAdapter adapter);

  void inject(MusicRecyclerViewAdapter adapter);

  //playlist
  void inject(FavouritesPlayList playList);

  void inject(MostPlayedPlayList playList);

  void inject(LastPlayedPlayList playList);

  // models
  void inject(EqualizerModel model);

  void inject(AllPlaylistModel allPlaylistModel);

  // presenters
  void inject(MyFilesPresenter presenter);

  void inject(AllSongsPresenter presenter);

  void inject(NowPlayingPresenter presenter);

  void inject(MusicPlayerPresenter presenter);

  void inject(AlbumPresenter presenter);

  void inject(ArtistPresenter presenter);

  void inject(GenresPresenter presenter);

  //activity
  void inject(AlbumActivity activity);

  void inject(ArtistActivity activity);

  void inject(GenreActivity activity);

  //fragments
  void inject(MusicPlayerFragment fragment);

  void inject(AddToPlayListDialogFragment fragment);

  void inject(PresetReverbDialogFragment fragment);

  void inject(PresetDialogFragment fragment);

  void inject(EqualizerFragment fragment);

  void inject(CoverArtsFragment fragment);

  void inject(AlbumListFragment fragment);

  void inject(AllSongsFragment fragment);

  void inject(MusicCategoryFragment fragment);

  void inject(ArtistListFragment fragment);

  void inject(MyFilesFragment fragment);

  void inject(GenresListFragment fragment);

  void inject(AllPlayListFragment fragment);

  void inject(MembersFragment fragment);

  void inject(NowPlayingFragment fragment);

  void inject(SettingsFragment fragment);

  MusicPlayerApp app();
}