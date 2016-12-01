package com.kingbull.musicplayer.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.kingbull.musicplayer.MusicPlayerApp;
import javax.inject.Inject;

public final class CallReceiver extends BroadcastReceiver {
  @Inject Player musicPlayer;

  Context context;

  public CallReceiver() {
    MusicPlayerApp.instance().component().inject(this);
  }

  public void onReceive(Context context, Intent intent) {
    try {
      this.context = context;
      TelephonyManager telephonyManager =
          (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
      telephonyManager.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    } catch (Exception e) {
      Log.e("Phone Receive Error", " " + e);
    }
  }

  private class MyPhoneStateListener extends PhoneStateListener {
    boolean isPaused = false;//paused when call arived then true else false

    public void onCallStateChanged(int state, String incomingNumber) {
      Log.d("MyPhoneListener", state + "   incoming no:" + incomingNumber);
      if (state == 1) {
        if (musicPlayer.isPlaying()) {
          musicPlayer.pause();
          isPaused = true;
        }
      } else if (state == 0) {
        if (isPaused) musicPlayer.play();
      }
    }
  }
}