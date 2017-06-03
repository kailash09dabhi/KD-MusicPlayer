package com.kingbull.musicplayer.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import javax.inject.Inject;

/**
 * This class is responsible for informing app when earphone is plugged in/out!
 * But android os does not allow to register the receiver with manifest for the action
 * Intent.ACTION_HEADSET_PLUG so it has to be explicitly register via code.
 *
 * @author Kailash Dabhi
 * @date 03 June, 2017 7:13 PM
 */
public final class HeadsetPlugReceiver extends BroadcastReceiver {
  private final static int PLUGGED = 1;
  private final static int UNPLUGGED = 0;
  private final IntentFilter intentFilter;
  @Inject Player player;
  @Inject SettingPreferences settingPreferences;

  public HeadsetPlugReceiver() {
    intentFilter = new IntentFilter();
    intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
      int state = intent.getIntExtra("state", -1);
      switch (state) {
        case UNPLUGGED:
          if (settingPreferences.pauseOnHeadsetUnplugged()) player.pause();
          break;
        case PLUGGED:
          if (settingPreferences.resumeOnHeadsetPlugged()) player.play();
          break;
        default:
          Log.d("HeadsetPlugReceiver", "I have no idea what the headset state is");
      }
    }
  }

  public IntentFilter intentFilter() {
    return intentFilter;
  }
}