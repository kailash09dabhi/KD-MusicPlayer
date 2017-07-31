package com.kingbull.musicplayer.ui.base;

import android.support.annotation.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import java.lang.ref.WeakReference;

public class Presenter<V> implements Mvp.Presenter<V> {
  protected CompositeDisposable compositeDisposable;
  private WeakReference<V> view;

  protected Presenter() {
    compositeDisposable = new CompositeDisposable();
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
