package com.kingbull.musicplayer.ui.music;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.drawable.RoundLayerDrawable;
import com.kingbull.musicplayer.ui.equalizer.EqualizerActivity;
import com.kingbull.musicplayer.ui.music.widget.ShadowImageView;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingFragment;
import com.kingbull.musicplayer.utils.AlbumUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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

  public static MusicPlayerFragment instance() {
    MusicPlayerFragment fragment = new MusicPlayerFragment();
    return fragment;
  }

  @OnClick(R.id.equalizerView) void onEqualizerClick() {
    presenter.onEqualizerClick();
  }

  @OnClick(R.id.nowPlayingView) void onNowPlayingClick() {
    getFragmentManager().beginTransaction()
        .add(android.R.id.content, new NowPlayingFragment())
        .addToBackStack(NowPlayingFragment.class.getSimpleName())
        .commit();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_music, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    equalizerView.setImageDrawable(new RoundLayerDrawable(R.drawable.ic_equalizer, Color.WHITE));
    nowPlayingView.setImageDrawable(new RoundLayerDrawable(R.drawable.ic_queue_music, Color.WHITE));
  }

  @Override public void onStop() {
    super.onStop();
    seekBarProgress.dontAnimate();
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

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .ofType(MusicEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<MusicEvent>() {
          @Override public void accept(MusicEvent musicEvent) {
            onSongUpdated(musicEvent.music());
            if (musicEvent.musicPlayerEvent() == MusicPlayerEvent.PLAY
                || musicEvent.musicPlayerEvent() == MusicPlayerEvent.PAUSE) {
              buttonPlayToggle.setImageResource(
                  musicEvent.musicPlayerEvent() == MusicPlayerEvent.PAUSE ? R.drawable.ic_pause
                      : R.drawable.ic_play);
              if (musicEvent.musicPlayerEvent() == MusicPlayerEvent.PLAY) {
                albumImageView.resumeRotateAnimation();
                seekBarProgress.startProgresssAnimation();
              } else {
                albumImageView.pauseRotateAnimation();
                seekBarProgress.dontAnimate();
              }
            }
          }
        });
  }

  @Override protected void onPresenterPrepared(MusicPlayer.Presenter presenter) {
    this.presenter.takeView(this);
    seekBarProgress.takePresenter(presenter);
    updatePlayMode(new SettingPreferences().musicMode());
  }

  @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.MusicPlayer();
  }

  @Override public void updateProgressDurationText(int duration) {
    progressTextView.setText(new Milliseconds(duration).toTimeString());
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

  @Override public void onSongUpdated(Music song) {
    nameTextView.setText(song.media().title());
    textViewArtist.setText(song.media().artist());
    // Step 2: favorite
    buttonFavoriteToggle.setImageResource(
        song.mediaStat().isFavorite() ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
    // Step 3: Duration
    durationTextView.setText(new Milliseconds(song.media().duration()).toTimeString());
    // Step 4: Keep these things updated
    // - Album rotation
    // - Progress(progressTextView & seekBarProgress)
    Glide.with(this)
        .load(new AlbumArt(song.media().path()))
        .asBitmap()
        .into(new SimpleTarget<Bitmap>() {
          @Override public void onResourceReady(Bitmap bitmap,
              GlideAnimation<? super Bitmap> glideAnimation) {
            albumImageView.setImageBitmap(AlbumUtils.getCroppedBitmap(bitmap));
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
                    updateUiWithPaletteSwatch(darkMutedSwatch, lightMutedSwatch);
                  } else if (darkVibrantSwatch != null && lightVibrantSwatch != null) {
                    updateUiWithPaletteSwatch(darkVibrantSwatch, lightVibrantSwatch);
                  } else if (vibrantSwatch != null && mutedSwatch != null) {
                    updateUiWithPaletteSwatch(vibrantSwatch, mutedSwatch);
                  }
                }
              }
            });
          }

          @Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
            albumImageView.setImageResource(R.drawable.default_record_album);
          }
        });
    seekBarProgress.updateMusic(song);
    albumImageView.startRotateAnimation();
    seekBarProgress.startProgresssAnimation();
  }

  private void updateUiWithPaletteSwatch(Palette.Swatch darkSwatch, Palette.Swatch lightSwatch) {
    getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(darkSwatch.getRgb()));
    getView().setBackgroundColor(darkSwatch.getRgb());
    nameTextView.setTextColor(lightSwatch.getRgb());
    textViewArtist.setTextColor(lightSwatch.getRgb());
    progressTextView.setTextColor(lightSwatch.getRgb());
    durationTextView.setTextColor(lightSwatch.getRgb());
  }

  @Override public void updatePlayMode(MusicMode musicMode) {
    playModeToggleView.takePlayMode(musicMode);
  }

  @Override public void updateFavoriteToggle(boolean favorite) {
    buttonFavoriteToggle.setImageResource(
        favorite ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
  }

  @Override public void updateSeekBar(int progress) {
    seekBarProgress.updateThumb(progress);
  }

  @Override public void updateSeekBarAfter(long updateProgressInterval) {
    seekBarProgress.animateProgressAfter(updateProgressInterval);
  }
}
