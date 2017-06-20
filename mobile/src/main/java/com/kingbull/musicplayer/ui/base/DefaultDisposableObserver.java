package com.kingbull.musicplayer.ui.base;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author Kailash Dabhi
 * @date 20 June, 2017 11:34 PM
 */
public final class DefaultDisposableObserver<T> extends DisposableObserver<T> {
  @Override public void onNext(@NonNull T t) {
  }

  @Override public void onError(@NonNull Throwable e) {
  }

  @Override public void onComplete() {
  }
}
