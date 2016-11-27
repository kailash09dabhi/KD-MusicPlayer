package com.kingbull.musicplayer.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import com.kingbull.musicplayer.domain.SettingPreferences;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/15/16
 * Time: 8:12 PM
 * Desc: BaseActivity
 */
public abstract class BaseActivity<P extends Presenter> extends AppCompatActivity {

  private static final int LOADER_ID = 9;
  private CompositeSubscription mSubscriptions;
  private Presenter presenter;

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (mSubscriptions != null) {
      mSubscriptions.clear();
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * An easy way to set up non-home(no back button on the toolbar) activity to enable
   * go back action.
   *
   * @param toolbar The toolbar with go back button
   * @return ActionBar
   */
  protected ActionBar supportActionBar(Toolbar toolbar) {
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }
    return actionBar;
  }

  protected void addSubscription(Subscription subscription) {
    if (subscription == null) return;
    if (mSubscriptions == null) {
      mSubscriptions = new CompositeSubscription();
    }
    mSubscriptions.add(subscription);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
    getSupportLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<P>() {
      @Override public final Loader<P> onCreateLoader(int id, Bundle args) {
        Log.i(this.getClass().getSimpleName(), "onCreateLoader");
        return new PresenterLoader<>(BaseActivity.this, presenterFactory());
      }

      @Override public final void onLoadFinished(Loader<P> loader, P presenter) {
        Log.i(this.getClass().getSimpleName(), "onLoadFinished");
        BaseActivity.this.presenter = presenter;
        onPresenterPrepared(presenter);
      }

      @Override public final void onLoaderReset(Loader<P> loader) {
        Log.i(this.getClass().getSimpleName(), "onLoaderReset");
        BaseActivity.this.presenter = null;
        //onPresenterDestroyed();
      }
    });
    addSubscription(subscribeEvents());
  }

  protected abstract void onPresenterPrepared(P presenter);

  /**
   * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance
   * should
   * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
   */
  @NonNull protected abstract PresenterFactory<P> presenterFactory();

  protected Subscription subscribeEvents() {
    return null;
  }

  @Override protected void onResume() {
    super.onResume();
    if (new SettingPreferences().isFullScreen()) {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
  }
}
