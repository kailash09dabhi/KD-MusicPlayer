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
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
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
  private final SettingPreferences settingPreferences = new SettingPreferences();
  @BindView(R.id.fullScreenCheckbox) CheckBox fullScreenCheckbox;
  @BindView(R.id.durationSecondsView) TextView durationSecondsView;
  @BindView(R.id.headerLayout) LinearLayout headerLayout;
  @BindView(R.id.scrollView) ScrollView scrollView;

  @OnClick(R.id.hideSmallClips) void onClickHideSmallClips() {
    new DurationFilterDialogFragment().show(getActivity().getSupportFragmentManager(),
        DurationFilterDialogFragment.class.getName());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings, null);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupView();
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof DurationFilterEvent) {
              durationSecondsView.setText(
                  new SettingPreferences().filterDurationInSeconds() + " sec");
            } else if (o instanceof PaletteEvent || o instanceof ThemeEvent) {
              applyUiColors();
            }
          }
        });
  }

  @Override protected PresenterFactory<Settings.Presenter> presenterFactory() {
    return new PresenterFactory.Settings();
  }

  @Override protected void onPresenterPrepared(Settings.Presenter presenter) {
    presenter.takeView(this);
  }

  private void setupView() {
    fullScreenCheckbox.setChecked(settingPreferences.isFullScreen());
    durationSecondsView.setText(settingPreferences.filterDurationInSeconds() + " sec");
    applyUiColors();
  }

  private void applyUiColors() {
    scrollView.setBackgroundColor(smartColorTheme.screen().intValue());
    headerLayout.setBackgroundColor(smartColorTheme.header().intValue());
    deepChangeTextColor((ViewGroup) getView());
  }

  public void deepChangeTextColor(ViewGroup parentLayout) {
    for (int count = 0; count < parentLayout.getChildCount(); count++) {
      View view = parentLayout.getChildAt(count);
      if (view instanceof TextView) {
        ((TextView) view).setTextColor(smartColorTheme.titleText().intValue());
      } else if (view instanceof ViewGroup) {
        deepChangeTextColor((ViewGroup) view);
      }
    }
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

  private void setAlarmToStopApp(long scheduleAt) {
    PendingIntent pendingIntent =
        PendingIntent.getService(getActivity(), 0, new Intent(MusicService.ACTION_STOP_SERVICE), 0);
    AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scheduleAt, pendingIntent);
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
    RxBus.getInstance().post(new ThemeEvent());
  }
}
