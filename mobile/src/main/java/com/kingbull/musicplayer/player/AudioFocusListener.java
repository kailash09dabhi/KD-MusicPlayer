package com.kingbull.musicplayer.player;

/**
 * @author Kailash Dabhi
 * @date 22 June, 2017 6:14 PM
 */
final class AudioFocusListener implements AudioFocus.Listener {
  private final Player player;
  private AudioFocus.State audioFocusState;

  public AudioFocusListener(Player player) {
    this.player = player;
  }

  @Override public void onGainedAudioFocus() {
    audioFocusState = AudioFocus.State.Focused;
    // restart media player with new focus settings
    //if (mState == State.Playing)
    //  configAndStartMediaPlayer();
    pauseOrPlayAndSetVolumeAccordingToFocus();
  }

  @Override public void onLostAudioFocus(boolean canDuck) {
    audioFocusState = canDuck ? AudioFocus.State.NoFocusCanDuck : AudioFocus.State.NoFocusNoDuck;
    pauseOrPlayAndSetVolumeAccordingToFocus();
  }

  private void pauseOrPlayAndSetVolumeAccordingToFocus() {
    if (player.isPlaying()) {
      if (audioFocusState == AudioFocus.State.NoFocusNoDuck) {
        // If we don't have audio focus and can't duck, we have to pause, even if mState
        // is State.Playing. But we stay in the Playing state so that we know we have to resume
        // playback once we get the focus back.
        if (player.isPlaying()) player.pause();
        return;
      } else if (audioFocusState == AudioFocus.State.NoFocusCanDuck) {
        player.setVolume(0.1f);// we'll be relatively quiet
      } else {
        player.setVolume(1.0f); // we can be loud
      }
      if (!player.isPlaying()) player.play();
    }
  }
}
