package com.kingbull.musicplayer.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import com.kingbull.musicplayer.di.AppModule;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.ui.base.musiclist.ringtone.Ringtone;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;
import javax.inject.Named;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity<P extends Mvp.Presenter> extends AppCompatActivity {
  private static final int LOADER_ID = 9;
  @Inject @Named(AppModule.FLAT_THEME) protected ColorTheme flatTheme;
  @Inject @Named(AppModule.SMART_THEME) protected ColorTheme smartTheme;
  protected P presenter;
  @Inject protected SettingPreferences settingPreferences;
  private CompositeDisposable compositeDisposable;

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
    addDisposable(subscribeEvents());
  }

  @Override protected void onDestroy() {
    if (compositeDisposable != null) {
      compositeDisposable.clear();
    }
    super.onDestroy();
  }

  /**
   * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance
   * should
   * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
   */
  @NonNull protected abstract PresenterFactory<P> presenterFactory();

  protected abstract void onPresenterPrepared(P presenter);

  protected void addDisposable(Disposable disposable) {
    if (disposable == null) {
      return;
    }
    if (compositeDisposable == null) {
      compositeDisposable = new CompositeDisposable();
    }
    compositeDisposable.add(disposable);
  }

  protected Disposable subscribeEvents() {
    return null;
  }

  @RequiresApi(api = Build.VERSION_CODES.M) @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Ringtone.PERMISSION_REQUEST_CODE) {
      if (Settings.System.canWrite(this)) {
        Ringtone.onPermissionGranted(this);
      } else {
        new Snackbar(findViewById(android.R.id.content)).show(
            "Please grant permission to set ringtone!");
      }
    }
  }

  @Override protected void onResume() {
    super.onResume();
    if (settingPreferences.isFullScreen()) {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case Ringtone.PERMISSION_REQUEST_CODE: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          Ringtone.onPermissionGranted(this);
        } else {
          new Snackbar(findViewById(android.R.id.content)).show(
              "Please grant permission to set ringtone!");
        }
        return;
      }
    }
  }
}
