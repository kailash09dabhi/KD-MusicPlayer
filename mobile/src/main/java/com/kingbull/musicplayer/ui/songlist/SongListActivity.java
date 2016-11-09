package com.kingbull.musicplayer.ui.songlist;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.utils.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class SongListActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {
  private static final int URL_LOAD_LOCAL_MUSIC = 0;
  private static final String ORDER_BY = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";
  private static String[] PROJECTIONS = {
      MediaStore.Audio.Media.DATA, // the real path
      MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.IS_RINGTONE, MediaStore.Audio.Media.IS_MUSIC,
      MediaStore.Audio.Media.IS_NOTIFICATION, MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.SIZE
  };
  @BindView(R.id.recyclerview_grid) RecyclerView recyclerView;
  private CompositeSubscription mSubscriptions;

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_list);
    ButterKnife.bind(this);
    mSubscriptions = new CompositeSubscription();
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    getSupportLoaderManager().initLoader(getIntent().getIntExtra("genre_id", 0), null, this);
    TextView nameView = (TextView) this.findViewById(R.id.shimmer_tv);
    nameView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    nameView.setSingleLine(true);
    nameView.setMarqueeRepeatLimit(5);
    nameView.setSelected(true);
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (id == 0) return null;
    return new CursorLoader(this, MediaStore.Audio.Genres.Members.getContentUri("external", id),
        PROJECTIONS, null, null, ORDER_BY);
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    Subscription subscription =
        Observable.just(data)
            .flatMap(new Func1<Cursor, Observable<List<Song>>>() {
              @Override public Observable<List<Song>> call(Cursor cursor) {
                List<Song> songs = new ArrayList<>();
                if (cursor != null && cursor.getCount() > 0) {
                  cursor.moveToFirst();
                  do {
                    Song song = cursorToMusic(cursor);
                    songs.add(song);
                  } while (cursor.moveToNext());
                }
                return Observable.just(songs);
              }
            })
            .doOnNext(new Action1<List<Song>>() {
              @Override public void call(List<Song> songs) {
                //Log.d(TAG, "onLoadFinished: " + songs.size());
                Collections.sort(songs, new Comparator<Song>() {
                  @Override public int compare(Song left, Song right) {
                    return left.getDisplayName().compareTo(right.getDisplayName());
                  }
                });
              }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<Song>>() {
              @Override public void onStart() {
                //mView.showProgress();
              }

              @Override public void onCompleted() {
                //mView.hideProgress();
              }

              @Override public void onError(Throwable throwable) {
                //mView.hideProgress();
                //Log.e(TAG, "onError: ", throwable);
              }

              @Override public void onNext(List<Song> songs) {
                recyclerView.setAdapter(new SongsAdapter(songs));
              }
            });
    mSubscriptions.add(subscription);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  private Song cursorToMusic(Cursor cursor) {
    String realPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
    File songFile = new File(realPath);
    Song song;
    if (songFile.exists()) {
      // Using song parsed from file to avoid encoding problems
      song = FileUtils.fileToMusic(songFile);
      if (song != null) {
        return song;
      }
    }
    song = new Song();
    song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
    String displayName =
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
    if (displayName.endsWith(".mp3")) {
      displayName = displayName.substring(0, displayName.length() - 4);
    }
    song.setDisplayName(displayName);
    song.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
    song.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
    song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
    song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
    song.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
    return song;
  }
}
