package com.kingbull.musicplayer.ui.main.categories.all;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.kingbull.musicplayer.RxSchedulerRule;
import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.domain.MediaStat;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.musiclist.AndroidMediaStoreDatabase;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Kailash Dabhi
 * @date 7/26/2017 11:53 AM
 */

public class AllSongsPresenterTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Rule public RxSchedulerRule testSchedulerRule = new RxSchedulerRule();
  @Mock AndroidMediaStoreDatabase mediaStoreDatabase;
  @Mock AllSongs.View allSongsView;
  AllSongsPresenter allSongsPresenter = new AllSongsPresenter();
  List<Music> songs = new ArrayList<Music>() {
    {
      for (int i = 0; i < 10; i++) {
        add(fakeMusic());
      }
    }
  };

  @Before public void setUp() throws Exception {
    allSongsPresenter.songs = songs;
    allSongsPresenter.androidMediaStoreDatabase = mediaStoreDatabase;
    allSongsPresenter.takeView(allSongsView);
  }

  @Test public void testOnDeleteSelectedMusic() throws Exception {
    when(allSongsView.selectedMusicList()).thenReturn(songs);
    allSongsPresenter.onDeleteSelectedMusic();
    Thread.sleep(1000);//let the thread work and then check
    assertEquals(0, songs.size());
  }

  @Test public void testIsSongListAvailable() {
    allSongsPresenter.songs = songs;
    assertEquals(true,allSongsPresenter.isSongListAvailable());
    allSongsPresenter.songs =null;
    assertEquals(false,allSongsPresenter.isSongListAvailable());
    allSongsPresenter.songs = new ArrayList<>();
    assertEquals(false,allSongsPresenter.isSongListAvailable());
  }

  private Music fakeMusic() {
    Music music = mock(Music.class);
    when(music.media()).thenReturn(Media.NONE);
    when(music.mediaStat()).thenReturn(mock(MediaStat.class));
    return music;
  }

}