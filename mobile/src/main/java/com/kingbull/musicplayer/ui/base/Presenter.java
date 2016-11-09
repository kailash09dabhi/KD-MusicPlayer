package com.kingbull.musicplayer.ui.base;

import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;

public abstract class Presenter<V> implements Mvp.Presenter<V> {
  private WeakReference<V> view;

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
