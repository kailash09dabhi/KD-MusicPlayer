package com.kingbull.musicplayer.player;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.MusicEvent;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import java.util.concurrent.TimeUnit;

/**
 * Represents {@link MusicEvent} stream.
 *
 * @author Kailash Dabhi
 * @date 08 May, 2017 12:41 PM
 */
public final class MusicEventRelay {
  private static volatile MusicEventRelay instance;
  private final Relay<MusicEvent> relay =
      PublishRelay.create();


  private MusicEventRelay() {
    relay.toSerialized();
    RxBus.getInstance().toObservable()
        .ofType(MusicEvent.class)
        .debounce(400, TimeUnit.MILLISECONDS)
        .subscribeWith(new DisposableObserver<MusicEvent>() {
          @Override public void onNext(@NonNull MusicEvent musicEvent) {
            relay.accept(musicEvent);
          }

          @Override public void onError(@NonNull Throwable e) {
          }

          @Override public void onComplete() {
          }
        });
  }

  public static MusicEventRelay instance() {
    if (instance == null) {
      synchronized (MusicEventRelay.class) {
        if (instance == null) {
          instance = new MusicEventRelay();
        }
      }
    }
    return instance;
  }

  public Observable<MusicEvent> asObservable() {
    return relay.hide();
  }
}