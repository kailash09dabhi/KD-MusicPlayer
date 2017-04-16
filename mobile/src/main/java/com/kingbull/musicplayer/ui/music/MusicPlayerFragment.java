package com.kingbull.musicplayer.ui.music;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
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
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.event.MusicEvent;
import com.kingbull.musicplayer.image.AlbumArt;
import com.kingbull.musicplayer.player.MusicMode;
import com.kingbull.musicplayer.player.MusicPlayerEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.BitmapImage;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.StatusBarColor;
import com.kingbull.musicplayer.ui.base.animators.Alpha;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.equalizer.EqualizerActivity;
import com.kingbull.musicplayer.ui.music.widget.ShadowImageView;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingFragment;
import com.kingbull.musicplayer.utils.AlbumUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public final class MusicPlayerFragment extends BaseFragment<MusicPlayer.Presenter>
    implements MusicPlayer.View {
  @BindView(R.id.equalizerView) ImageView equalizerView;
  @BindView(R.id.nowPlayingView) ImageView nowPlayingView;
  @BindView(R.id.albumImageView) ShadowImageView albumImageView;
  @BindView(R.id.nameTextView) TextView nameTextView;
  @BindView(R.id.text_view_artist) TextView textViewArtist;
  @BindView(R.id.progressTextView) TextView progressTextView;
  @BindView(R.id.durationTextView) TextView durationTextView;
  @BindView(R.id.seekbar) MusicSeekBar seekBarProgress;
  @BindView(R.id.button_play_mode_toggle) PlayModeToggleView playModeToggleView;
  @BindView(R.id.button_play_toggle) ImageView buttonPlayToggle;
  @BindView(R.id.button_favorite_toggle) ImageView buttonFavoriteToggle;
  @BindView(R.id.backgroundView) View backgroundView;
  StatusBarColor statusBarColor;

  public static MusicPlayerFragment newInstance() {
    MusicPlayerFragment fragment = new MusicPlayerFragment();
    return fragment;
  }

  @OnClick(R.id.equalizerView) void onEqualizerClick() {
    presenter.onEqualizerClick();
  }

  @OnClick(R.id.nowPlayingView) void onNowPlayingClick() {
    getFragmentManager().beginTransaction()
        .add(android.R.id.content, NowPlayingFragment.newInstance(statusBarColor.intValue()))
        .addToBackStack(NowPlayingFragment.class.getSimpleName())
        .commit();
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
    com.kingbull.musicplayer.ui.base.Color color =
        new com.kingbull.musicplayer.ui.base.Color(flatTheme.screen().intValue());
    applyColorTheme(flatTheme.header().intValue(), color.light(5).toDrawable().getColor());
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .ofType(MusicEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<MusicEvent>() {
          @Override public void accept(MusicEvent musicEvent) {
            if (musicEvent.musicPlayerEvent() == MusicPlayerEvent.PLAY
                || musicEvent.musicPlayerEvent() == MusicPlayerEvent.PAUSE) {
              buttonPlayToggle.setImageResource(
                  musicEvent.musicPlayerEvent() == MusicPlayerEvent.PAUSE ? R.drawable.ic_play
                      : R.drawable.ic_pause);
              if (musicEvent.musicPlayerEvent() == MusicPlayerEvent.PLAY) {
                albumImageView.resumeRotateAnimation();
                seekBarProgress.startProgresssAnimation();
              } else {
                albumImageView.pauseRotateAnimation();
                seekBarProgress.dontAnimate();
              }
            } else {
              onSongUpdated(musicEvent.music());
            }
          }
        });
  }

  @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.MusicPlayer();
  }

  @Override protected void onPresenterPrepared(MusicPlayer.Presenter presenter) {
    this.presenter.takeView(this);
    seekBarProgress.takePresenter(presenter);
    updatePlayMode(new SettingPreferences().musicMode());
  }

  @Override public void onSongUpdated(Music song) {
    nameTextView.setText(song.media().title());
    textViewArtist.setText(song.media().artist());
    // Step 2: favorite
    buttonFavoriteToggle.setImageResource(
        song.mediaStat().isFavorite() ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
    // Step 3: Duration
    durationTextView.setText(new Milliseconds(song.media().duration()).toString());
    // Step 4: Keep these things updated
    // - Album rotation
    // - Progress(progressTextView & seekBarProgress)
    Glide.with(this)
        .load(new AlbumArt(song.media().path()))
        .asBitmap()
        .into(new SimpleTarget<Bitmap>() {
          @Override public void onResourceReady(Bitmap bitmap,
              GlideAnimation<? super Bitmap> glideAnimation) {
            setAlbumImageAndAnimateBackground(bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
              public void onGenerated(Palette palette) {
                if (palette != null) {
                  Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                  Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                  Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();
                  Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
                  Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                  Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                  if (darkMutedSwatch != null && lightMutedSwatch != null) {
                    applyColorTheme(darkMutedSwatch.getRgb(), lightMutedSwatch.getRgb());
                  } else if (darkVibrantSwatch != null && lightVibrantSwatch != null) {
                    applyColorTheme(darkVibrantSwatch.getRgb(), lightVibrantSwatch.getRgb());
                  } else if (vibrantSwatch != null && mutedSwatch != null) {
                    applyColorTheme(vibrantSwatch.getRgb(), mutedSwatch.getRgb());
                  } else {
                    applyColorTheme(flatTheme.header().intValue(), flatTheme.bodyText().intValue());
                  }
                }
              }
            });
          }

          @Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.k3);
            setAlbumImageAndAnimateBackground(bitmap);
          }
        });
    seekBarProgress.updateMusic(song);
    albumImageView.startRotateAnimation();
    seekBarProgress.startProgresssAnimation();
  }

  private void setAlbumImageAndAnimateBackground(Bitmap bitmap) {
    albumImageView.setImageBitmap(AlbumUtils.circularBitmap(bitmap));
    Observable.just(bitmap)
        .map(new Function<Bitmap, BitmapDrawable>() {
          @Override public BitmapDrawable apply(Bitmap bitmap) throws Exception {
            return new BitmapImage(bitmap, getResources()).blurred(52)
                .saturated()
                .asBitmapDrawable();
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<BitmapDrawable>() {
          @Override public void onNext(BitmapDrawable bitmap) {
            backgroundView.setBackground(bitmap);
            new Alpha.Animation(2500).animateIn(backgroundView, null);
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onComplete() {
          }
        });
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

  @Override public void showEqualizerScreen() {
    Intent intent = new Intent(getActivity(), EqualizerActivity.class);
    startActivity(intent);
  }

  @Override public void pause() {
    albumImageView.pauseRotateAnimation();
    seekBarProgress.dontAnimate();
  }

  @Override public void play() {
    albumImageView.resumeRotateAnimation();
    seekBarProgress.startProgresssAnimation();
  }

  private void applyColorTheme(int darkColor, int lightColor) {
    statusBarColor = new StatusBarColor(darkColor);
    statusBarColor.applyOn(getActivity().getWindow());
    nameTextView.setTextColor(lightColor);
    textViewArtist.setTextColor(lightColor);
    progressTextView.setTextColor(lightColor);
    durationTextView.setTextColor(lightColor);
    equalizerView.setImageDrawable(
        new IconDrawable(R.drawable.ic_equalizer, Color.WHITE, darkColor));
    nowPlayingView.setImageDrawable(
        new IconDrawable(R.drawable.ic_queue_music, Color.WHITE, darkColor));
  }

  @OnClick(R.id.button_play_toggle) public void onPlayToggleAction(View view) {
    presenter.onPlayToggleClick();
  }

  @OnClick(R.id.button_play_mode_toggle) public void onPlayModeToggleAction(View view) {
    presenter.onPlayModeToggleClick();
  }

  @OnClick(R.id.button_play_next) public void onPlayNextAction(View view) {
    presenter.onPlayNextClick();
  }

  @OnClick(R.id.button_play_previous) public void onPlayPreviousClick() {
    presenter.onPlayPreviousClick();
  }

  @OnClick(R.id.button_favorite_toggle) public void onFavoriteToggleAction(View view) {
    presenter.onFavoriteToggleClick();
  }
}
