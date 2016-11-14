package com.kingbull.musicplayer.ui.base;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

import static android.R.attr.tag;

public final class PresenterLoader<T extends Presenter> extends Loader<T> {

  private final PresenterFactory<T> factory;
  private T presenter;

  public PresenterLoader(Context context, PresenterFactory<T> factory) {
    super(context);
    this.factory = factory;
  }

  @Override protected void onStartLoading() {
    Log.i("loader", "onStartLoading-" + tag);
    // if we already own a presenter instance, simply deliver it.
    if (presenter != null) {
      deliverResult(presenter);
      return;
    }
    // Otherwise, force a load
    forceLoad();
  }

  @Override protected void onForceLoad() {
    Log.i("loader", "onForceLoad-" + tag);
    // Create the Presenter using the Factory
    presenter = factory.create();
    // Deliver the result
    deliverResult(presenter);
  }

  @Override public void deliverResult(T data) {
    super.deliverResult(data);
    Log.i("loader", "deliverResult-" + tag);
  }

  @Override protected void onStopLoading() {
    Log.i("loader", "onStopLoading-" + tag);
  }

  @Override protected void onReset() {
    Log.i("loader", "onReset-" + tag);
    if (presenter != null) {
      //presenter.onDestroyed();
      presenter = null;
    }
  }
}