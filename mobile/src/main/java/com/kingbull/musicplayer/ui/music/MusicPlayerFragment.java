package com.kingbull.musicplayer.ui.music;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
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
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.PreferenceManager;
import com.kingbull.musicplayer.player.PlayMode;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.equalizer.EqualizerActivity;
import com.kingbull.musicplayer.ui.nowplaying.NowPlayingFragment;
import com.kingbull.musicplayer.ui.widget.ShadowImageView;
import com.kingbull.musicplayer.utils.AlbumUtils;
import com.kingbull.musicplayer.utils.TimeUtils;
import rx.Subscription;

import static com.kingbull.musicplayer.R.id.text_view_artist;

public final class MusicPlayerFragment extends BaseFragment<MusicPlayer.Presenter>
    implements MusicPlayer.View {

  @BindView(R.id.image_view_album) ShadowImageView imageViewAlbum;
  @BindView(R.id.text_view_name) TextView textViewName;
  @BindView(text_view_artist) TextView textViewArtist;
  @BindView(R.id.text_view_progress) TextView textViewProgress;
  @BindView(R.id.text_view_duration) TextView textViewDuration;
  @BindView(R.id.seek_bar) MusicSeekBar seekBarProgress;
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

  @OnClick(R.id.button_favorite_toggle) public void onFavoriteToggleAction(View view) {
    presenter.onFavoriteToggleClick();
  }

  @Override protected Subscription subscribeEvents() {
    return null;
  }

  @Override protected void onPresenterPrepared(MusicPlayer.Presenter presenter) {
    this.presenter.takeView(this);
    seekBarProgress.takePresenter(presenter);
    updatePlayMode(PreferenceManager.lastPlayMode(getActivity()));
  }

  @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.MusicPlayer();
  }

  @Override public void updateProgressDurationText(int duration) {
    textViewProgress.setText(TimeUtils.formatDuration(duration));
  }

  @Override public void onPlayStatusChanged(boolean isPlaying) {
    buttonPlayToggle.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
    if (isPlaying) {
      imageViewAlbum.resumeRotateAnimation();
      seekBarProgress.dontAnimate();
      seekBarProgress.startProgresssAnimation();
    } else {
      imageViewAlbum.pauseRotateAnimation();
      seekBarProgress.dontAnimate();
    }
  }

  @Override public void stopSeekbarProgress() {
    seekBarProgress.dontAnimate();
    seekBarProgress.startProgresssAnimation();
  }

  @Override public void showEqualizerScreen(int audioSessionId) {
    Intent intent = new Intent(getActivity(), EqualizerActivity.class);
    intent.putExtra("audio_session_id", audioSessionId);
    startActivity(intent);
  }

  @Override public void onSongUpdated(Music song) {
    textViewName.setText(song.title());
    textViewArtist.setText(song.artist());
    // Step 2: favorite
    buttonFavoriteToggle.setImageResource(
        song.isFavorite() ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
    // Step 3: Duration
    textViewDuration.setText(TimeUtils.formatDuration(song.duration()));
    // Step 4: Keep these things updated
    // - Album rotation
    // - Progress(textViewProgress & seekBarProgress)
    Bitmap bitmap = AlbumUtils.parseAlbum(song);
    if (bitmap == null) {
      imageViewAlbum.setImageResource(R.drawable.default_record_album);
    } else {
      bitmap = AlbumUtils.getCroppedBitmap(bitmap);
      imageViewAlbum.setImageBitmap(bitmap);
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
              updateUiWithPalleteSwatch(darkMutedSwatch, lightMutedSwatch);
            } else if (darkVibrantSwatch != null && lightVibrantSwatch != null) {
              updateUiWithPalleteSwatch(darkVibrantSwatch, lightVibrantSwatch);
            } else if (vibrantSwatch != null && mutedSwatch != null) {
              updateUiWithPalleteSwatch(vibrantSwatch, mutedSwatch);
            }
          }
        }
      });
    }
    imageViewAlbum.pauseRotateAnimation();
    seekBarProgress.dontAnimate();
    seekBarProgress.updateMusic(song);
  }

  private void updateUiWithPalleteSwatch(Palette.Swatch darkSwatch, Palette.Swatch lightSwatch) {
    getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(darkSwatch.getRgb()));
    getView().setBackgroundColor(darkSwatch.getRgb());
    textViewName.setTextColor(lightSwatch.getRgb());
    textViewArtist.setTextColor(lightSwatch.getRgb());
    textViewProgress.setTextColor(lightSwatch.getRgb());
    textViewDuration.setTextColor(lightSwatch.getRgb());
  }

  @Override public void updatePlayMode(PlayMode playMode) {
    playModeToggleView.takePlayMode(playMode);
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
