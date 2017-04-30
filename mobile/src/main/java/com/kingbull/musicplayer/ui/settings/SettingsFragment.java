package com.kingbull.musicplayer.ui.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.google.android.gms.ads.AdListener;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.BlurRadiusEvent;
import com.kingbull.musicplayer.event.DurationFilterEvent;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.player.MusicService;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.ads.AdmobInterstitial;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.Calendar;

/**
 * @author Kailash Dabhi
 * @date 27th Nov, 2016
 */
public final class SettingsFragment extends BaseFragment<Settings.Presenter>
    implements Settings.View {
  @BindView(R.id.fullScreenCheckbox) CheckBox fullScreenCheckbox;
  @BindView(R.id.flatThemeCheckbox) CheckBox flatThemeCheckbox;
  @BindView(R.id.durationSecondsView) TextView durationSecondsView;
  @BindView(R.id.headerLayout) LinearLayout headerLayout;
  @BindView(R.id.scrollView) ScrollView scrollView;
  @BindView(R.id.appVersionView) TextView appVersionView;
  private AdmobInterstitial admobInterstitial;

  @OnClick(R.id.hideSmallClips) void onClickHideSmallClips() {
    DurationFilterDialogFragment.newInstance()
        .show(getActivity().getSupportFragmentManager(),
            DurationFilterDialogFragment.class.getName());
  }

  @OnClick(R.id.feedback) void onClickFeedback() {
    composeEmail(new String[] { "kingbulltechnology@gmail.com" }, "KD MusicPlayer Feedback");
  }

  private void composeEmail(String[] addresses, String subject) {
    Intent intent = new Intent(Intent.ACTION_SENDTO);
    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
    intent.putExtra(Intent.EXTRA_EMAIL, addresses);
    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
      startActivity(intent);
    }
  }

  @OnClick(R.id.blurBackground) void onClickBlurRadius() {
    BlurRadiusDialogFragment.newInstance()
        .show(getActivity().getSupportFragmentManager(), BlurRadiusDialogFragment.class.getName());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings, container, false);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    return view;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupView();
    setupAdmobInterstial();
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof DurationFilterEvent) {
              durationSecondsView.setText(settingPreferences.filterDurationInSeconds() + " sec");
            } else if (o instanceof PaletteEvent || o instanceof ThemeEvent) {
              applyUiColors();
            } else if (o instanceof BlurRadiusEvent) {
              admobInterstitial.showIfLoaded();
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
    flatThemeCheckbox.setChecked(settingPreferences.isFlatTheme());
    durationSecondsView.setText(settingPreferences.filterDurationInSeconds() + " sec");
    appVersionView.setText(MusicPlayerApp.instance().versionName() + " (BETA)");
    applyUiColors();
  }

  private void applyUiColors() {
    scrollView.setBackgroundColor(smartTheme.screen().intValue());
    headerLayout.setBackgroundColor(smartTheme.header().intValue());
    deepChangeTextColor((ViewGroup) getView());
  }

  private void deepChangeTextColor(ViewGroup parentLayout) {
    for (int count = 0; count < parentLayout.getChildCount(); count++) {
      View view = parentLayout.getChildAt(count);
      if (view instanceof TextView) {
        ((TextView) view).setTextColor(smartTheme.titleText().intValue());
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
    Window window = getActivity().getWindow();
    if (isChecked) {
      settingPreferences.saveFullScreen(true);
      window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
      window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    } else {
      window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
      settingPreferences.saveFullScreen(false);
    }
  }

  private void setupAdmobInterstial() {
    admobInterstitial = new AdmobInterstitial(getActivity(),
        getResources().getString(R.string.kd_music_player_settings_interstitial), new AdListener() {
      @Override public void onAdClosed() {
        admobInterstitial.load();
      }
    });
    admobInterstitial.load();
  }

  @OnCheckedChanged(R.id.flatThemeCheckbox) void onThemeCheckedChange(boolean isChecked) {
    settingPreferences.saveFlatTheme(isChecked);
    RxBus.getInstance().post(new ThemeEvent());
  }
}
