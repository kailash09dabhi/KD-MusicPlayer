package com.kingbull.musicplayer.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/16/16
 * Time: 12:14 AM
 * Desc: BaseFragment
 */
public abstract class BaseFragment<P extends Presenter> extends Fragment {

  private static final int LOADER_ID = 101;
  private Presenter presenter;
  private CompositeSubscription mSubscriptions;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    addSubscription(subscribeEvents());
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (mSubscriptions != null) {
      mSubscriptions.clear();
    }
  }

  protected Subscription subscribeEvents() {
    return null;
  }

  protected void addSubscription(Subscription subscription) {
    if (subscription == null) return;
    if (mSubscriptions == null) {
      mSubscriptions = new CompositeSubscription();
    }
    mSubscriptions.add(subscription);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.i(this.getClass().getSimpleName(), "onActivityCreated-");
    // LoaderCallbacks as an object, so no hint regarding loader will be leak to the subclasses.
    getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<P>() {
      @Override public final Loader<P> onCreateLoader(int id, Bundle args) {
        Log.i(this.getClass().getSimpleName(), "onCreateLoader-");
        return new PresenterLoader<>(getContext(), presenterFactory());
      }

      @Override public final void onLoadFinished(Loader<P> loader, P presenter) {
        Log.i(this.getClass().getSimpleName(), "onLoadFinished-");
        BaseFragment.this.presenter = presenter;
        onPresenterPrepared(presenter);
      }

      @Override public final void onLoaderReset(Loader<P> loader) {
        Log.i(this.getClass().getSimpleName(), "onLoaderReset-");
        BaseFragment.this.presenter = null;
        //onPresenterDestroyed();
      }
    });
  }

  protected abstract void onPresenterPrepared(P presenter);

  /**
   * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance
   * should
   * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
   */
  @NonNull protected abstract PresenterFactory<P> presenterFactory();
}
