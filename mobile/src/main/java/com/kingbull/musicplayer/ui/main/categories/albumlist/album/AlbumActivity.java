package com.kingbull.musicplayer.ui.main.categories.albumlist.album;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.di.StorageModule;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.ImageFile;
import com.kingbull.musicplayer.domain.storage.StorageDirectory;
import com.kingbull.musicplayer.event.CoverArtDownloadedEvent;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.image.GlideApp;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.Image;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.StatusBarColor;
import com.kingbull.musicplayer.ui.base.ads.AdmobBannerLoaded;
import com.kingbull.musicplayer.ui.base.analytics.Analytics;
import com.kingbull.musicplayer.ui.base.animators.Alpha;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.base.musiclist.OnSelectionListener;
import com.kingbull.musicplayer.ui.base.view.SelectionOptionsLayout;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import com.kingbull.musicplayer.ui.coverarts.CoverArtsFragment;
import com.kingbull.musicplayer.ui.main.Pictures;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import com.kingbull.musicplayer.ui.sorted.SortDialogFragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class AlbumActivity extends BaseActivity<Album.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, Album.View {

  private static final int PICK_COVER_ART_GALLERY = 9;
  private final Alpha.Animation alphaAnimation = new Alpha.Animation();
  private final List<Music> songList = new ArrayList<>();
  private final StorageDirectory coverArtDir = new StorageDirectory(StorageModule.COVER_ART_DIR);
  private final Pictures pictures = new Pictures();
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.selectionContextOptionsLayout) SelectionOptionsLayout selectionOptionsLayout;
  @BindView(R.id.sortButton) ImageView sortButton;
  @BindView(R.id.shuffleButton) ImageView shuffleButton;
  @BindView(R.id.albumart) ImageView albumArtView;
  @BindView(R.id.totaltime) TextView totalTimeView;
  @BindView(R.id.totaltracks) TextView totalTracks;
  @BindView(R.id.artistname) TextView artistNameView;
  @BindView(R.id.rootView) View rootView;
  @Inject Analytics analytics;
  private MusicRecyclerViewAdapter adapter;
  private com.kingbull.musicplayer.domain.Album album;

  @OnClick(R.id.albumart) void onCoverArtClick() {
    presenter.onCoverArtClick();
  }

  @OnClick(R.id.sortButton) void onSortClick() {
    presenter.onSortMenuClick();
  }

  @OnClick(R.id.shuffleButton) void onShuffleClick() {
    presenter.onShuffleMenuClick();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (VERSION.SDK_INT >= VERSION_CODES.M) {
      super.onActivityResult(requestCode, resultCode, data);
    }
    if (requestCode == PICK_COVER_ART_GALLERY && resultCode == Activity.RESULT_OK) {
      if (data == null) {
        //Display an error
        new Snackbar(recyclerView).show("Sorry to say but we couldn't fetch the album art!");
        return;
      }
      GlideApp.with(this).asBitmap().load(data.getData()).into(new SimpleTarget<Bitmap>(300, 300) {
        @Override public void onResourceReady(@NonNull Bitmap bitmap,
            @Nullable Transition<? super Bitmap> transition) {
          try {
            final File file = new File(coverArtDir.asFile(), album.name() + ".jpg");
            new ImageFile(file).save(bitmap);
            album = album.saveCoverArt(file.getPath());
            new Snackbar(recyclerView).show("Cover art saved successfully!");
            showAlbumArt();
          } catch (IOException e) {
            new Snackbar(recyclerView).show("Sorry but cover art not saved:(");
          }
        }
      });
    }
  }

  private void showAlbumArt() {
    File file = null;
    if (!TextUtils.isEmpty(album.albumArt())) {
      file = new File(album.albumArt());
    }
    GlideApp.with(this)
        .asBitmap()
        .load(album.albumArt())
        .placeholder(pictures.random())
        .error(pictures.random())
        .centerCrop()
        .signature(
            new ObjectKey(file == null ? "" : (file.length() + "@" + file.lastModified())))
        .into(new SimpleTarget<Bitmap>(300, 300) {
          @Override public void onResourceReady(@NonNull Bitmap bitmap,
              @Nullable Transition<? super Bitmap> transition) {
            albumArtView.setImageBitmap(bitmap);
            Observable.just(bitmap)
                .map(bitmap12 -> new Image.Smart(bitmap12)
                    .blurred()
                    .bitmapDrawable())
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

          @Override public void onLoadFailed(@Nullable Drawable errorDrawable) {
            super.onLoadFailed(errorDrawable);
            albumArtView.setImageDrawable(errorDrawable);
            Bitmap bitmap = ((BitmapDrawable) errorDrawable).getBitmap();
            Observable.just(bitmap)
                .map(bitmap1 -> new Image.Smart(bitmap1)
                    .blurred()
                    .bitmapDrawable())
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

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_album);
    ButterKnife.bind(this);
    MusicPlayerApp.instance().component().inject(this);
    new AdmobBannerLoaded((ViewGroup) findViewById(android.R.id.content));
    new StatusBarColor(flatTheme.statusBar()).applyOn(getWindow());
    album = getIntent().getParcelableExtra("album");
    adapter = new MusicRecyclerViewAdapter(songList, this);
    adapter.addOnSelectionListener(new OnSelectionListener() {
      @Override public void onClearSelection() {
        hideSelectionContextOptions();
      }

      @Override public void onMultiSelection(int selectionCount) {
        if (selectionCount == 1) {
          alphaAnimation.fadeOut(titleView);
          alphaAnimation.fadeIn(selectionOptionsLayout);
        }
      }
    });
    selectionOptionsLayout.addOnContextOptionClickListener(
        new SelectionOptionsLayout.OnContextOptionClickListener() {
          @Override public void onAddToPlaylistClick() {
            presenter.onAddToPlayListMenuClick();
          }

          @Override public void onDeleteSelectedClick() {
            presenter.onDeleteSelectedMusicClick();
          }

          @Override public void onClearSelectionClick() {
            presenter.onClearSelectionClick();
          }
        });
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    recyclerView.setBackgroundColor(flatTheme.header().transparent(0.999f).intValue());
    getSupportLoaderManager().initLoader(0, null, this);
    titleView.setText(album.name());
    titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    titleView.setSingleLine(true);
    titleView.setMarqueeRepeatLimit(-1);
    titleView.setSelected(true);
    showAlbumArt();
    int fillColor = 0;
    sortButton.setImageDrawable(new IconDrawable(R.drawable.ic_sort_48dp, fillColor));
    shuffleButton.setImageDrawable(new IconDrawable(R.drawable.ic_shuffle_48dp, fillColor));
    selectionOptionsLayout.updateIconsColor(fillColor);
    selectionOptionsLayout.updateIconSize(IconDrawable.dpToPx(40));
    analytics.logScreen(AlbumActivity.class.getSimpleName());
  }

  @NonNull @Override protected PresenterFactory<Album.Presenter> presenterFactory() {
    return new PresenterFactory.Album();
  }

  @Override protected void onPresenterPrepared(Album.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> {
          if (o instanceof CoverArtDownloadedEvent) {
            showAlbumArt();
          } else if (o instanceof SortEvent) {
            presenter.onSortEvent((SortEvent) o);
          }
        });
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AlbumSongsCursorLoader(this, album.albumId(), settingPreferences);
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
    final CharSequence[] items = {"Gallery", "Internet"};
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Pick From");
    builder.setItems(items, (dialog, item) -> {
      if (item == 0) {
        presenter.onPickFromGalleryClick();
      } else if (item == 1) {
        presenter.onPickFromInternetClick();
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

  @Override public void showSortMusicListDialog() {
    SortDialogFragment.newInstance()
        .show(getSupportFragmentManager(), SortDialogFragment.class.getName());
  }

  @Override public void showAddToPlayListDialog() {
    AddToPlayListDialogFragment.newInstance(adapter.getSelectedMusics())
        .show(getSupportFragmentManager(), AddToPlayListDialogFragment.class.getName());
    adapter.clearSelection();
  }

  @Override public void showMusicScreen() {
    startActivity(new Intent(this, MusicPlayerActivity.class));
  }

  @Override public void clearSelection() {
    adapter.clearSelection();
    hideSelectionContextOptions();
  }

  @Override public List<Music> selectedMusicList() {
    return adapter.getSelectedMusics();
  }

  @Override public void removeFromList(Music music) {
    adapter.notifyItemRemoved(songList.indexOf(music));
    songList.remove(music);
  }

  @Override public void showMessage(String message) {
    new Snackbar(recyclerView).show(message);
  }

  @Override public void hideSelectionContextOptions() {
    alphaAnimation.fadeOut(selectionOptionsLayout);
    alphaAnimation.fadeIn(titleView);
  }
}
