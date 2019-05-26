package com.kingbull.musicplayer.ui.main.categories.all;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.domain.MediaStat;
import com.kingbull.musicplayer.domain.Music;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Kailash Dabhi
 * @date 7/26/2017 11:53 AM
 */

public class DeleteFileConsumerTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  List<Music> songs = new ArrayList<Music>() {
    {
      for (int i = 0; i < 100; i++) {
        add(fakeMusic());
      }
    }
  };

  @Before public void setUp() throws Exception {
  }

  @Test(expected = IllegalStateException.class)  public void testDeleteMusic() throws Exception {
    List<Music> selectedMusic = new ArrayList<Music>(){
      {
        add(songs.get(5));
        add(songs.get(25));
        add(songs.get(45));
        add(songs.get(75));
        add(songs.get(95));
        add(songs.get(95));
      }
    };
    for (Music music : selectedMusic) {
      int position = songs.indexOf(music);
      if (position == -1) {
        throw new IllegalStateException("How songs.indexOf returning -1?");
      } else {
        songs.remove(music);
      }
    }
    assertEquals(95, songs.size());
  }

  @Test public void testDeleteMusics() throws Exception {
    List<Music> selectedMusic = new ArrayList<Music>(){
      {
        add(songs.get(95));
        add(songs.get(45));
        add(songs.get(5));
        add(songs.get(25));
        add(songs.get(75));
      }
    };
    Observable.just(selectedMusic).delay(25, TimeUnit.MILLISECONDS).doOnNext(
            musicList -> {
              for (Music music : musicList) {
                int position = songs.indexOf(music);
                if (position == -1) {
                  throw new IllegalStateException("How songs.indexOf returning -1?");
                } else {
                  songs.remove(music);
                }
              }
            }).doOnComplete(() -> assertEquals(95, songs.size())).subscribe();

  }

  private Music fakeMusic() {
    Music music = mock(Music.class);
    when(music.media()).thenReturn(Media.NONE);
    when(music.mediaStat()).thenReturn(mock(MediaStat.class));
    return music;
  }

}