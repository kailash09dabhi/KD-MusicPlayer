/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.event.DurationFilterEvent;
import com.kingbull.musicplayer.event.ThemeChangedEvent;
import com.kingbull.musicplayer.player.MusicService;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.Calendar;

public final class SettingsFragment extends BaseFragment<Settings.Presenter>
    implements Settings.View {

  SettingPreferences settingPreferences = new SettingPreferences();
  @BindView(R.id.fullScreenCheckbox) CheckBox fullScreenCheckbox;
  @BindView(R.id.durationSecondsView) TextView durationSecondsView;

  @OnClick(R.id.hideSmallClips) void onClickHideSmallClips() {
    new DurationFilterDialogFragment().show(getActivity().getSupportFragmentManager(),
        DurationFilterDialogFragment.class.getName());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings, null);
    ButterKnife.bind(this, view);
    setupView(view);
    return view;
  }

  private void setupView(View v) {
    fullScreenCheckbox.setChecked(settingPreferences.isFullScreen());
    durationSecondsView.setText(new SettingPreferences().filterDurationInSeconds() + " sec");
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .ofType(DurationFilterEvent.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<DurationFilterEvent>() {
          @Override public void accept(DurationFilterEvent sortEvent) {
            durationSecondsView.setText(
                new SettingPreferences().filterDurationInSeconds() + " sec");
          }
        });
  }

  @Override protected void onPresenterPrepared(Settings.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override protected PresenterFactory<Settings.Presenter> presenterFactory() {
    return new PresenterFactory.Settings();
  }

  @OnClick(R.id.sleepTimerView) void onSleepTimerClick() {
    Calendar currentTime = Calendar.getInstance();
    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
    int minute = currentTime.get(Calendar.MINUTE);
    TimePickerDialog timePickerDialog;
    timePickerDialog =
        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
          @Override
          public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
            int diffHour = selectedHour - currentHour;
            int diffMinute = selectedMinute - currentMinute;
            setAlarmToStopApp(diffHour * 60 * 60 * 1000 + diffMinute * 60 * 1000);
          }
        }, hour, minute, false);//Yes 24 hour time
    timePickerDialog.setTitle("Select Time");
    timePickerDialog.show();
  }

  @OnCheckedChanged(R.id.fullScreenCheckbox) void onFullScreenCheckedChange(boolean isChecked) {
    if (isChecked) {
      settingPreferences.saveFullScreen(true);
      getActivity().getWindow()
          .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    } else {
      settingPreferences.saveFullScreen(false);
      new Snackbar(getActivity().findViewById(android.R.id.content)).show(
          "Restart your app to see " + "the effect!");
    }
  }

  @OnCheckedChanged(R.id.flatThemeCheckbox) void onThemeCheckedChange(boolean isChecked) {
    settingPreferences.saveFlatTheme(isChecked);
    RxBus.getInstance().post(new ThemeChangedEvent());
  }

  private void setAlarmToStopApp(long scheduleAt) {
    PendingIntent pendingIntent =
        PendingIntent.getService(getActivity(), 0, new Intent(MusicService.ACTION_STOP_SERVICE), 0);
    AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scheduleAt, pendingIntent);
  }
}
