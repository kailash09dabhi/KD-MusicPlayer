package com.kingbull.musicplayer.ui.music;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.sqlite.table.AlbumTable;
import com.kingbull.musicplayer.event.MusicEvent;
import com.kingbull.musicplayer.image.GlideBitmapPool;
import com.kingbull.musicplayer.player.MusicEventRelay;
import com.kingbull.musicplayer.player.MusicMode;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.Image.Smart;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.StatusBarColor;
import com.kingbull.musicplayer.ui.base.ads.AdmobInterstitial;
import com.kingbull.musicplayer.ui.base.animators.Alpha;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.equalizer.EqualizerActivity;
import com.kingbull.musicplayer.ui.main.Pictures;
import com.kingbull.musicplayer.ui.music.widget.ShadowImageView;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingFragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.File;

public final class MusicPlayerFragment extends BaseFragment<MusicPlayer.Presenter>
    implements MusicPlayer.View {
  private final AlbumTable albumTable = new AlbumTable();
  private final Pictures pictures = new Pictures();
  @BindView(R.id.equalizerView) ImageView equalizerView;
  @BindView(R.id.nowPlayingView) ImageView nowPlayingView;
  @BindView(R.id.albumImageView) ShadowImageView albumImageView;
  @BindView(R.id.nameView) TextView nameView;
  @BindView(R.id.artistView) TextView artistView;
  @BindView(R.id.progressTextView) TextView progressTextView;
  @BindView(R.id.durationTextView) TextView durationTextView;
  @BindView(R.id.seekbar) MusicSeekBar seekBarProgress;
  @BindView(R.id.button_play_mode_toggle) PlayModeToggleView playModeToggleView;
  @BindView(R.id.button_play_toggle) ImageView buttonPlayToggle;
  @BindView(R.id.button_favorite_toggle) ImageView buttonFavoriteToggle;
  @BindView(R.id.backgroundView) View backgroundView;
  private StatusBarColor statusBarColor;
  private AdmobInterstitial equalizerInterstitial;
  private AdmobInterstitial nowPlayingListInterstitial;
  private SimpleTarget<Bitmap> albumBitmapSimpleTarget = new SimpleTarget<Bitmap>() {
    @Override
    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
      setAlbumImageAndAnimateBackground(bitmap);
    }

    @Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
      setAlbumImageAndAnimateBackground(((BitmapDrawable) errorDrawable).getBitmap());
    }
  };
  private final ComponentCallbacks2 componentCallback = new ComponentCallbacks2() {
    @Override public void onTrimMemory(int level) {
      Glide.with(MusicPlayerFragment.this).onTrimMemory(level);
      Glide.clear(albumBitmapSimpleTarget);
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override public void onLowMemory() {
      Glide.with(MusicPlayerFragment.this).onLowMemory();
      Glide.clear(albumBitmapSimpleTarget);
    }
  };

  public static MusicPlayerFragment newInstance() {
    MusicPlayerFragment fragment = new MusicPlayerFragment();
    return fragment;
  }

  @OnClick(R.id.equalizerView) void onEqualizerClick() {
    presenter.onEqualizerClick();
  }

  @OnClick(R.id.nowPlayingView) void onNowPlayingClick() {
    presenter.onNowPlayingClick();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_music, container, false);
  }

  @Override public void onStop() {
    super.onStop();
    seekBarProgress.dontAnimate();
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    getActivity().registerComponentCallbacks(componentCallback);
    setupInterstitial();
  }

  @Override protected Disposable subscribeEvents() {
    return MusicEventRelay.instance().asObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<MusicEvent>() {
          @Override public void accept(MusicEvent musicEvent) {
            presenter.onMusicEvent(musicEvent);
          }
        });
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    applyColorTheme(flatTheme.header().intValue());
  }

  @Override public void onDestroyView() {
    getActivity().unregisterComponentCallbacks(componentCallback);
    super.onDestroyView();
  }

  @Override protected PresenterFactory<MusicPlayer.Presenter> presenterFactory() {
    return new PresenterFactory.MusicPlayer();
  }

  @Override protected void onPresenterPrepared(MusicPlayer.Presenter presenter) {
    this.presenter.takeView(this);
    seekBarProgress.takePresenter(presenter);
    updatePlayMode(settingPreferences.musicMode());
  }

  @Override public void updatePlayMode(MusicMode musicMode) {
    playModeToggleView.takePlayMode(musicMode);
  }

  @Override public void updateFavoriteToggle(boolean favorite) {
    buttonFavoriteToggle.setImageResource(
        favorite ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
  }

  @Override public void updateProgressDurationText(int duration) {
    progressTextView.setText(new Milliseconds(duration).toString());
  }

  @Override public void updateSeekBar(int progress) {
    seekBarProgress.updateThumb(progress);
  }

  @Override public void updateSeekBarAfter(long updateProgressInterval) {
    seekBarProgress.animateProgressAfter(updateProgressInterval);
  }

  @Override public void stopSeekbarProgress() {
    seekBarProgress.dontAnimate();
    seekBarProgress.startProgresssAnimation();
  }

  @Override public void gotoShowEqualizerScreen() {
    if (equalizerInterstitial.isLoaded()) {
      equalizerInterstitial.show();
    } else {
      launchEqualizerScreen();
    }
  }

  @Override public void pause() {
    albumImageView.pauseRotateAnimation();
    seekBarProgress.dontAnimate();
  }

  @Override public void play() {
    albumImageView.resumeRotateAnimation();
    seekBarProgress.startProgresssAnimation();
  }

  @Override public void displayPauseButton() {
    buttonPlayToggle.setImageResource(R.drawable.ic_pause);
  }

  @Override public void displayPlayButton() {
    buttonPlayToggle.setImageResource(R.drawable.ic_play);
  }

  @Override public void startAlbumImageRotationAnimation() {
    albumImageView.resumeRotateAnimation();
  }

  @Override public void startProgressAnimation() {
    seekBarProgress.startProgresssAnimation();
  }

  @Override public void stopAlbumImageRotationAnimation() {
    albumImageView.pauseRotateAnimation();
  }

  @Override public void stopProgressAnimation() {
    seekBarProgress.dontAnimate();
  }

  @Override public void displayNewSongInfo(Music song) {
    nameView.setText(song.media().title());
    artistView.setText(song.media().artist());
    buttonFavoriteToggle.setImageResource(
        song.mediaStat().isFavorite() ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
    durationTextView.setText(new Milliseconds(song.media().duration()).toString());
    seekBarProgress.updateMusic(song);
    albumImageView.startRotateAnimation();
    seekBarProgress.startProgresssAnimation();
    final Album album = albumTable.albumById(song.media().albumId());
    File file = null;
    if (!TextUtils.isEmpty(album.albumArt())) {
      file = new File(album.albumArt());
    }
    Glide.with(this)
        .load(albumTable.albumById(song.media().albumId()).albumArt())
        .asBitmap()
        .placeholder(pictures.toDrawablesId()[0])
        .error(pictures.random())
        .centerCrop()
        .signature(
            new StringSignature(file == null ? "" : (file.length() + "@" + file.lastModified())))
        .into(albumBitmapSimpleTarget);
  }

  @Override public void gotoNowPlayingListScreen() {
    if (nowPlayingListInterstitial.isLoaded()) {
      nowPlayingListInterstitial.show();
    } else {
      launchNowPlayingListScreen();
    }
  }

  @Override public void close() {
    getActivity().finish();
  }

  private void applyColorTheme(int darkColor) {
    statusBarColor = new StatusBarColor(darkColor);
    statusBarColor.applyOn(getActivity().getWindow());
    nameView.setTextColor(Color.WHITE);
    artistView.setTextColor(Color.WHITE);
    progressTextView.setTextColor(Color.WHITE);
    durationTextView.setTextColor(Color.WHITE);
    equalizerView.setImageDrawable(new IconDrawable(R.drawable.ic_equalizer, darkColor));
    nowPlayingView.setImageDrawable(new IconDrawable(R.drawable.ic_queue_music, darkColor));
  }

  private void setupInterstitial() {
    equalizerInterstitial = new AdmobInterstitial(getActivity(),
        getResources().getString(R.string.kd_music_player_settings_interstitial),
        new AdmobInterstitial.AdListener() {
          @Override public void onAdClosed() {
            if (!getActivity().isFinishing()) {
              equalizerInterstitial.load();
              launchEqualizerScreen();
            }
          }
        });
    equalizerInterstitial.load();
    nowPlayingListInterstitial = new AdmobInterstitial(getActivity(),
        getResources().getString(R.string.kd_music_player_settings_interstitial),
        new AdmobInterstitial.AdListener() {
          @Override public void onAdClosed() {
            if (!getActivity().isFinishing()) {
              nowPlayingListInterstitial.load();
              launchNowPlayingListScreen();
            }
          }
        });
    nowPlayingListInterstitial.load();
  }

  private void launchEqualizerScreen() {
    Intent intent = new Intent(getActivity(), EqualizerActivity.class);
    startActivity(intent);
  }

  private void launchNowPlayingListScreen() {
    getFragmentManager().beginTransaction()
        .add(android.R.id.content, NowPlayingFragment.newInstance(statusBarColor.intValue()))
        .addToBackStack(NowPlayingFragment.class.getSimpleName())
        .commitAllowingStateLoss();
  }

  private Bitmap lastBlurredRecyclableBitmap;

  private void setAlbumImageAndAnimateBackground(Bitmap bitmap) {
    RoundedBitmapDrawable circularBitmapDrawable =
        RoundedBitmapDrawableFactory.create(getResources(), bitmap);
    circularBitmapDrawable.setCircular(true);
    albumImageView.setImageDrawable(circularBitmapDrawable);
    Observable.just(bitmap)
        .map(new Function<Bitmap, BitmapDrawable>() {
          @Override public BitmapDrawable apply(Bitmap bitmap) throws Exception {
            if (lastBlurredRecyclableBitmap != null && !lastBlurredRecyclableBitmap.isRecycled()) {
              GlideBitmapPool.instance().put(lastBlurredRecyclableBitmap);
            }
            Smart smartImage = new Smart(bitmap)
                .blurred(52)
                .saturated();
            lastBlurredRecyclableBitmap = smartImage
                .bitmap();
            return smartImage.bitmapDrawable();
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<BitmapDrawable>() {
          @Override public void onNext(BitmapDrawable bitmap) {
            backgroundView.setBackground(bitmap);
            new Alpha.Animation(0.16f, 2500).fadeIn(backgroundView);
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onComplete() {
          }
        });
  }

  @OnClick(R.id.button_play_toggle) public void onPlayToggleAction() {
    presenter.onPlayToggleClick();
  }

  @OnClick(R.id.button_play_mode_toggle) public void onPlayModeToggleAction() {
    presenter.onPlayModeToggleClick();
  }

  @OnClick(R.id.button_play_next) public void onPlayNextAction() {
    presenter.onPlayNextClick();
  }

  @OnClick(R.id.button_play_previous) public void onPlayPreviousClick() {
    presenter.onPlayPreviousClick();
  }

  @OnClick(R.id.button_favorite_toggle) public void onFavoriteToggleAction() {
    presenter.onFavoriteToggleClick();
  }
}
