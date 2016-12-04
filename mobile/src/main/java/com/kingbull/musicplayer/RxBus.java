package com.kingbull.musicplayer;

import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;
import org.reactivestreams.Subscriber;

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

  private final PublishSubject<Object> bus = PublishSubject.create();

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

  /**
   * A simple logger for RxBus which can also prevent
   * potential crash(OnErrorNotImplementedException) caused by error in the workflow.
   */
  public static <T> Subscriber<T> defaultSubscriber(Class<T> type) {
    return new DisposableSubscriber<T>() {
      @Override public void onError(Throwable e) {
        Log.e(TAG, "What is this? Please solve this as soon as possible!", e);
      }

      @Override public void onComplete() {
      }

      @Override public void onNext(T o) {
        Log.d(TAG, "New event received: " + o);
      }
    };
  }

  @NonNull public static <T> DisposableObserver<T> defaultSubscriber() {
    return new DisposableObserver<T>() {
      @Override public void onNext(T value) {
        Log.d(TAG, "New event received: " + value);
      }

      @Override public void onError(Throwable e) {
        Log.e(TAG, "What is this? Please solve this as soon as possible!", e);
      }

      @Override public void onComplete() {
      }
    };
  }

  public void post(Object event) {
    bus.onNext(event);
  }

  public io.reactivex.Observable<Object> toObservable() {
    return bus;
  }
}
