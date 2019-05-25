package com.kingbull.musicplayer.ui.base;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.util.Log;
import android.view.View;
import com.kingbull.musicplayer.di.AppModule;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;
import javax.inject.Named;

public abstract class BaseFragment<P extends Mvp.Presenter> extends Fragment {
  private static final int LOADER_ID = 101;
  @Inject @Named(AppModule.SMART_THEME) protected ColorTheme smartTheme;
  @Inject @Named(AppModule.FLAT_THEME) protected ColorTheme flatTheme;
  @Inject protected SettingPreferences settingPreferences;
  protected P presenter;
  private CompositeDisposable compositeDisposable;

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    addSubscription(subscribeEvents());
  }

  private void addSubscription(Disposable subscription) {
    if (subscription == null) return;
    if (compositeDisposable == null) {
      compositeDisposable = new CompositeDisposable();
    }
    compositeDisposable.add(subscription);
  }

  protected Disposable subscribeEvents() {
    return null;
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

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (compositeDisposable != null) {
      compositeDisposable.clear();
    }
  }

  /**
   * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance
   * should
   * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
   */
  protected abstract PresenterFactory<P> presenterFactory();

  protected abstract void onPresenterPrepared(P presenter);
}
