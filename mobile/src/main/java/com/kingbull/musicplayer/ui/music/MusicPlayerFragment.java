package com.kingbull.musicplayer.ui.music;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.kingbull.musicplayer.domain.PreferenceManager;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.player.PlayMode;
import com.kingbull.musicplayer.player.PlaybackService;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.widget.ShadowImageView;
import com.kingbull.musicplayer.utils.AlbumUtils;
import com.kingbull.musicplayer.utils.TimeUtils;
import rx.Subscription;

import static com.kingbull.musicplayer.R.id.text_view_artist;

public final class MusicPlayerFragment extends BaseFragment implements MusicPlayer.View {

  @BindView(R.id.image_view_album) ShadowImageView imageViewAlbum;
  @BindView(R.id.text_view_name) TextView textViewName;
  @BindView(text_view_artist) TextView textViewArtist;
  @BindView(R.id.text_view_progress) TextView textViewProgress;
  @BindView(R.id.text_view_duration) TextView textViewDuration;
  @BindView(R.id.seek_bar) MusicSeekBar seekBarProgress;

  @BindView(R.id.button_play_mode_toggle) PlayModeToggleView playModeToggleView;
  @BindView(R.id.button_play_toggle) ImageView buttonPlayToggle;
  @BindView(R.id.button_favorite_toggle) ImageView buttonFavoriteToggle;
  Song song;
  PlaybackServiceConnection playbackServiceConnection;
  private MusicPlayer.Presenter presenter;
  private boolean mIsServiceBound;

  public static MusicPlayerFragment instance(Song song) {
    MusicPlayerFragment fragment = new MusicPlayerFragment();
    fragment.song = song;
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_music, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    presenter = new MusicPlayerPresenter();
    presenter.takeView(this);
    presenter.onTakeSong(song);
    seekBarProgress.takePresenter(presenter);
    seekBarProgress.updateMusic(song);
    playbackServiceConnection = new PlaybackServiceConnection(presenter);
    updatePlayMode(PreferenceManager.lastPlayMode(getActivity()));
    bindPlaybackService();
  }

  @Override public void onStop() {
    super.onStop();
    seekBarProgress.dontAnimate();
  }

  @Override public void onDestroyView() {
    if (mIsServiceBound) {
      // Detach our existing connection.
      getActivity().unbindService(playbackServiceConnection);
      mIsServiceBound = false;
    }
    presenter.takeView(null);
    presenter = null;
    super.onDestroyView();
  }

  @OnClick(R.id.button_play_toggle) public void onPlayToggleAction(View view) {
    presenter.onPlayToggleClick();
  }

  @OnClick(R.id.button_play_mode_toggle) public void onPlayModeToggleAction(View view) {
    presenter.onPlayModeToggleClick();
  }

  @OnClick(R.id.button_play_last) public void onPlayLastAction(View view) {
    presenter.onPlayLastClick();
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

  public void onSongUpdated(@Nullable Song song) {
    if (song == null) {
      imageViewAlbum.cancelRotateAnimation();
      buttonPlayToggle.setImageResource(R.drawable.ic_play);
      seekBarProgress.setProgress(0);
      updateProgressDurationText(0);
      presenter.seekTo(0);
      seekBarProgress.dontAnimate();
      return;
    }
    // Step 1: Song name and artist
    textViewName.setText(song.getDisplayName());
    textViewArtist.setText(song.getArtist());
    // Step 2: favorite
    buttonFavoriteToggle.setImageResource(
        song.isFavorite() ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
    // Step 3: Duration
    textViewDuration.setText(TimeUtils.formatDuration(song.getDuration()));
    // Step 4: Keep these things updated
    // - Album rotation
    // - Progress(textViewProgress & seekBarProgress)
    Bitmap bitmap = AlbumUtils.parseAlbum(song);
    if (bitmap == null) {
      imageViewAlbum.setImageResource(R.drawable.default_record_album);
    } else {
      bitmap = AlbumUtils.getCroppedBitmap(bitmap);
      imageViewAlbum.setImageBitmap(bitmap);
    }
    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
      public void onGenerated(Palette palette) {
        Palette.Swatch vibrantSwatch = palette.getDarkMutedSwatch();
        if (vibrantSwatch != null) {
          getActivity().getWindow()
              .setBackgroundDrawable(new ColorDrawable(palette.getDarkMutedSwatch().getRgb()));
          getView().setBackgroundColor(palette.getDarkMutedSwatch().getRgb());
          textViewName.setTextColor(palette.getLightMutedSwatch().getRgb());
          textViewArtist.setTextColor(palette.getLightMutedSwatch().getRgb());
          textViewProgress.setTextColor(palette.getLightMutedSwatch().getRgb());
          textViewDuration.setTextColor(palette.getLightMutedSwatch().getRgb());

          //playModeToggleView.setImageDrawable(DrawableHelper.withContext(getContext())
          //    .withColor(palette.getVibrantSwatch().getRgb())
          //    .withDrawable(R.drawable.ic_play_mode_shuffle)
          //    .tint()
          //    .get());
          //buttonPlayToggle.setImageDrawable(DrawableHelper.withContext(getContext())
          //    .withColor(palette.getVibrantSwatch().getRgb())
          //    .withDrawable(R.drawable.ic_play)
          //    .tint()
          //    .get());
          //buttonFavoriteToggle.setImageDrawable(DrawableHelper.withContext(getContext())
          //    .withColor(palette.getVibrantSwatch().getRgb())
          //    .withDrawable(R.drawable.ic_favorite_yes)
          //    .tint()
          //    .get());
          //buttonFavoriteToggle.set
          //          @BindView(R.id.button_play_mode_toggle) ImageView playModeToggleView;
          //          @BindView(R.id.button_play_toggle) ImageView buttonPlayToggle;
          //          @BindView(R.id.button_favorite_toggle) ImageView buttonFavoriteToggle;
        }
      }
    });
    imageViewAlbum.pauseRotateAnimation();
    seekBarProgress.dontAnimate();
    //if (player.isPlaying()) {
    //  imageViewAlbum.startRotateAnimation();
    //  handler.post(progressRunnable);
    //  buttonPlayToggle.setImageResource(R.drawable.ic_pause);
    //}
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

  private void bindPlaybackService() {
    // Establish a connection with the service.  We use an explicit
    // class name because we want a specific service implementation that
    // we know will be running in our own process (and thus won't be
    // supporting component replacement by other applications).
    getActivity().bindService(new Intent(getActivity(), PlaybackService.class),
        playbackServiceConnection, Context.BIND_AUTO_CREATE);
    mIsServiceBound = true;
  }
}
