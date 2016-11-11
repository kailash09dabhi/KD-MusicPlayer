package com.kingbull.musicplayer.ui.music;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.kingbull.musicplayer.player.PlaybackService;

/**
 * @author Kailash Dabhi
 * @date 11/11/2016.
 */

public final class PlaybackServiceConnection implements ServiceConnection {
  private final MusicPlayer.Presenter presenter;

  PlaybackServiceConnection(MusicPlayer.Presenter presenter) {
    this.presenter = presenter;
  }

  public void onServiceConnected(ComponentName className, IBinder service) {
    // This is called when the connection with the service has been
    // established, giving us the service object we can use to
    // interact with the service.  Because we have bound to a explicit
    // service that we know is running in our own process, we can
    // cast its IBinder to a concrete class and directly access it.
    presenter.onTakePlayBack(((PlaybackService.LocalBinder) service).getService());
  }

  public void onServiceDisconnected(ComponentName className) {
    // This is called when the connection with the service has been
    // unexpectedly disconnected -- that is, its process crashed.
    // Because it is running in our same process, we should never
    // see this happen.
    presenter.onTakePlayBack(null);
  }
}
