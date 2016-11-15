package com.kingbull.musicplayer.ui.base;

import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;
import rx.subscriptions.CompositeSubscription;

public abstract class Presenter<V> implements Mvp.Presenter<V> {
  protected CompositeSubscription compositeSubscription;
  private WeakReference<V> view;

  public Presenter() {
    compositeSubscription = new CompositeSubscription();
  }

  @Override public void takeView(@NonNull V view) {
    this.view = new WeakReference<>(view);
  }

  @Override public boolean hasView() {
    return view() != null;
  }

  @Override public V view() {
    if (view == null) {
      return null;
    } else {
      return view.get();
    }
  }
}
