package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.di.StorageModule;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.ImageFile;
import com.kingbull.musicplayer.domain.storage.StorageDirectory;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.event.CoverArtDownloadedEvent;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import com.kingbull.musicplayer.ui.coverarts.CoverArtsFragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class AlbumActivity extends BaseActivity<Album.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, Album.View {

  private final int PICK_COVER_ART_GALLERY = 9;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.songMenu) SongListRayMenu songListRayMenu;
  @BindView(R.id.albumart) ImageView albumArtView;
  @BindView(R.id.totaltime) TextView totalTimeView;
  @BindView(R.id.totaltracks) TextView totalTracks;
  @BindView(R.id.artistname) TextView artistNameView;
  @BindView(R.id.rootView) View rootView;
  MusicRecyclerViewAdapter adapter;
  List<Music> songList = new ArrayList<>();
  StorageDirectory coverArtDir = new StorageDirectory(StorageModule.COVER_ART_DIR);
  private com.kingbull.musicplayer.domain.Album album;

  @OnClick(R.id.albumart) void onCoverArtClick() {
    presenter.onCoverArtClick();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_COVER_ART_GALLERY && resultCode == Activity.RESULT_OK) {
      if (data == null) {
        //Display an error
        new Snackbar(recyclerView).show("Sorry to say but we couldnt fetch the album art!");
        return;
      }
      Glide.with(this).load(data.getData()).asBitmap().into(new SimpleTarget<Bitmap>(300, 300) {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
          try {
            final File file = new File(coverArtDir.asFile(), album.name() + ".jpg");
            new ImageFile(file).save(bitmap);
            album = album.saveCoverArt(file.getPath());
            new Snackbar(recyclerView).show("Cover art saved successfully!");
            showAlbumArt();
          } catch (FileNotFoundException e) {
            new Snackbar(recyclerView).show("Sorry but cover art not saved:(");
          } catch (IOException e) {
            new Snackbar(recyclerView).show("Sorry but cover art not saved:(");
          }
        }
      });
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_album);
    ButterKnife.bind(this);
    com.kingbull.musicplayer.ui.base.Color color =
        new com.kingbull.musicplayer.ui.base.Color(new SettingPreferences().windowColor());
    getWindow().setBackgroundDrawable(color.toDrawable());
    album = getIntent().getParcelableExtra("album");
    adapter = new MusicRecyclerViewAdapter(songList, this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    getSupportLoaderManager().initLoader(0, null, this);
    titleView.setText(album.name());
    titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    titleView.setSingleLine(true);
    titleView.setMarqueeRepeatLimit(-1);
    titleView.setSelected(true);
    songListRayMenu.addOnMenuClickListener(new SongListRayMenu.OnMenuClickListener() {

      @Override public void onShuffleMenuClick() {
        //presenter.onShuffleMenuClick();
      }

      @Override public void onAddToPlaylistMenuClick() {
        //presenter.onAddToPlayListMenuClick();
      }

      @Override public void onSortMenuClick() {
        //presenter.onSortMenuClick();
      }
    });
    showAlbumArt();
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .ofType(CoverArtDownloadedEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<CoverArtDownloadedEvent>() {
          @Override public void accept(CoverArtDownloadedEvent coverArtDownloadedEvent) {
            showAlbumArt();
          }
        });
  }

  private void showAlbumArt() {
    File file = null;
    if (!TextUtils.isEmpty(album.albumArt())) file = new File(album.albumArt());
    Glide.with(this)
        .load(album.albumArt())
        .asBitmap()
        .placeholder(R.drawable.a11)
        .error(R.drawable.a9)
        .centerCrop()
        .signature(
            new StringSignature(file == null ? "" : (file.length() + "@" + file.lastModified())))
        .into(new SimpleTarget<Bitmap>(300, 300) {
          @Override public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            albumArtView.setImageBitmap(bitmap);
            Observable.just(bitmap)
                .map(new Function<Bitmap, BitmapDrawable>() {
                  @Override public BitmapDrawable apply(Bitmap bitmap) throws Exception {
                    return new ImagePath("").toBlurredBitmap(bitmap, getResources());
                  }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<BitmapDrawable>() {
                  @Override public void onNext(BitmapDrawable bitmap) {
                    rootView.setBackground(bitmap);
                  }

                  @Override public void onError(Throwable e) {
                  }

                  @Override public void onComplete() {
                  }
                });
          }

          @Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
            super.onLoadFailed(e, errorDrawable);
            albumArtView.setImageDrawable(errorDrawable);
            Bitmap bitmap = ((BitmapDrawable) errorDrawable).getBitmap();
            Observable.just(bitmap)
                .map(new Function<Bitmap, BitmapDrawable>() {
                  @Override public BitmapDrawable apply(Bitmap bitmap) throws Exception {
                    return new ImagePath("").toBlurredBitmap(bitmap, getResources());
                  }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<BitmapDrawable>() {
                  @Override public void onNext(BitmapDrawable bitmap) {
                    rootView.setBackground(bitmap);
                  }

                  @Override public void onError(Throwable e) {
                  }

                  @Override public void onComplete() {
                  }
                });
          }
        });
  }

  @Override protected void onPresenterPrepared(Album.Presenter presenter) {
    presenter.takeView(this);
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.Album();
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AlbumSongsCursorLoader(this, album.albumId());
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    presenter.onSongCursorLoadFinished(data);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override public void showSongs(List<Music> songs) {
    songList.clear();
    songList.addAll(songs);
    adapter.notifyDataSetChanged();
    if (!songs.isEmpty()) {
      artistNameView.setText(String.format("By: %s", songs.get(0).media().artist()));
    }
    totalTracks.setText(String.format("Total Tracks: %d", songs.size()));
  }

  @Override public void showTotalDuration(String duration) {
    totalTimeView.setText(String.format("Total Time: %s", duration));
  }

  @Override public void showPickOptions() {
    final CharSequence[] items = { "Gallery", "Internet" };
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Pick From");
    builder.setItems(items, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int item) {
        if (item == 0) {
          presenter.onPickFromGalleryClick();
        } else if (item == 1) {
          presenter.onPickFromInternetClick();
        }
      }
    });
    builder.show();
  }

  @Override public void gotoGalleryScreen() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(intent, PICK_COVER_ART_GALLERY);
  }

  @Override public void gotoInternetCoverArtsScreen() {
    getSupportFragmentManager().beginTransaction()
        .add(android.R.id.content, CoverArtsFragment.newInstanceOfAlbumCovers(album),
            CoverArtsFragment.class.getSimpleName())
        .addToBackStack(CoverArtsFragment.class.getSimpleName())
        .commit();
  }
}
