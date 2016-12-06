package com.kingbull.musicplayer;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import io.reactivex.Observable;

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
