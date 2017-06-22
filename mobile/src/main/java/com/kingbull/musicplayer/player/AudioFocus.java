/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kingbull.musicplayer.player;

import android.content.Context;
import android.media.AudioManager;

/**
 * Convenience class to deal with audio focus. This class deals with everything related to audio
 * focus: it can request and abandon focus, and will intercept focus change events and deliver
 * them to a MusicFocusable interface (which, in our case, is implemented by {@link MusicService}).
 *
 * This class can only be used on SDK level 8 and above, since it uses API features that are not
 * available on previous SDK's.
 */
final class AudioFocus implements AudioManager.OnAudioFocusChangeListener {
  AudioManager audioManager;
  Listener listener;

  public AudioFocus(Context ctx, Listener focusable) {
    audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
    listener = focusable;
  }

  /** Requests audio focus. Returns whether request was successful or not. */
  public boolean requestFocus() {
    return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.requestAudioFocus(this,
        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
  }

  /** Abandons audio focus. Returns whether request was successful or not. */
  public boolean abandonFocus() {
    return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
  }

  /**
   * Called by AudioManager on audio focus changes. We implement this by calling our
   * MusicFocusable appropriately to relay the message.
   */
  public void onAudioFocusChange(int focusChange) {
    if (listener == null) return;
    switch (focusChange) {
      case AudioManager.AUDIOFOCUS_GAIN:
        listener.onGainedAudioFocus();
        break;
      case AudioManager.AUDIOFOCUS_LOSS:
      case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
        listener.onLostAudioFocus(false);
        break;
      case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
        listener.onLostAudioFocus(true);
        break;
      default:
    }
  }

  // do we have audio focus?
  enum State {
    NoFocusNoDuck,    // we don't have audio focus, and can't duck
    NoFocusCanDuck,   // we don't have focus, but can play at a low volume ("ducking")
    Focused           // we have full audio focus ; ;
  }

  /**
   * Represents something that can react to audio focus events. We implement this instead of just
   * using AudioManager.OnAudioFocusChangeListener because that interface is only available in SDK
   * level 8 and above, and we want our application to work on previous SDKs.
   */
  interface Listener {
    /** Signals that audio focus was gained. */
    void onGainedAudioFocus();

    /**
     * Signals that audio focus was lost.
     *
     * @param canDuck If true, audio can continue in "ducked" mode (low volume). Otherwise, all
     * audio must stop.
     */
    void onLostAudioFocus(boolean canDuck);
  }
}