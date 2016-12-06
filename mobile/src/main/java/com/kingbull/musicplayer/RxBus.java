package com.kingbull.musicplayer;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import io.reactivex.Observable;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/25/16
 * Time: 3:01 PM
 * Desc: An EventBus powered by RxJava.
 * But before you use this RxBus, bear in mind this very IMPORTANT note:
 * - Be very careful when error occurred here, this can terminate the whole
 * event observer pattern. If one error ever happened, new events won't be
 * received because this subscription has be terminated after onError(Throwable).
 */

public final class RxBus {

  private static final String TAG = "RxBus";

  private static volatile RxBus sInstance;

  private final Relay<Object> bus = PublishRelay.create().toSerialized();

  public static RxBus getInstance() {
    if (sInstance == null) {
      synchronized (RxBus.class) {
        if (sInstance == null) {
          sInstance = new RxBus();
        }
      }
    }
    return sInstance;
  }

  public void post(Object event) {
    bus.accept(event);
  }

  public Observable<Object> toObservable() {
    return bus;
  }
}
