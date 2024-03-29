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
import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kingbull.musicplayer.BuildConfig;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.ProLink;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.BackgroundEvent;
import com.kingbull.musicplayer.event.BlurRadiusEvent;
import com.kingbull.musicplayer.event.DurationFilterEvent;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.event.TransparencyChangedEvent;
import com.kingbull.musicplayer.player.BroadcastActionNames;
import com.kingbull.musicplayer.ui.base.AdListener;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.ads.AdmobBannerLoaded;
import com.kingbull.musicplayer.ui.base.ads.AdmobInterstitial;
import com.kingbull.musicplayer.ui.base.analytics.Analytics;
import com.kingbull.musicplayer.ui.settings.background.BackgroundsDialogFragment;
import com.kingbull.musicplayer.ui.settings.transparency.TransparencyDialogFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.Calendar;
import javax.inject.Inject;

/**
 * Represents Settings screen.
 *
 * @author Kailash Dabhi
 * @date 27th Nov, 2016
 */
public final class SettingsFragment extends BaseFragment<Settings.Presenter>
    implements Settings.View {
  private final BroadcastActionNames broadcastActionNames = new BroadcastActionNames();
  @BindView(R.id.fullScreenCheckbox) CheckBox fullScreenCheckbox;
  @BindView(R.id.flatThemeCheckbox) CheckBox flatThemeCheckbox;
  @BindView(R.id.durationSecondsView) TextView durationSecondsView;
  @BindView(R.id.headerLayout) LinearLayout headerLayout;
  @BindView(R.id.scrollView) ScrollView scrollView;
  @BindView(R.id.appVersionView) TextView appVersionView;
  @BindView(R.id.removeAdsView) TextView removeAdsView;
  @BindView(R.id.pauseOnHeadsetUnplugged) CheckBox pauseOnHeadsetUnplugged;
  @BindView(R.id.resumeOnHeadsetPlugged) CheckBox resumeOnHeadsetPlugged;
  @Inject Analytics analytics;
  private AdmobInterstitial admobInterstitial;

  @OnClick(R.id.hideSmallClips) void onClickHideSmallClips() {
    DurationFilterDialogFragment.newInstance()
        .show(getActivity().getSupportFragmentManager(),
            DurationFilterDialogFragment.class.getName());
  }

  @OnClick(R.id.backgrounds) void onBackgroundsClick() {
    BackgroundsDialogFragment.newInstance()
        .show(getActivity().getSupportFragmentManager(),
            BackgroundsDialogFragment.class.getName());
  }

  @OnClick(R.id.transparency) void onTransparencyClick() {
    TransparencyDialogFragment.newInstance()
        .show(getActivity().getSupportFragmentManager(),
            TransparencyDialogFragment.class.getName());
  }

  @OnClick(R.id.removeAdsView) void onClickRemoveAds() {
    new ProLink.PlayStore(getContext()).open();
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

  @OnClick(R.id.share) void onClickShare() {
    share();
  }

  public void share() {
    //create the send intent
    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
    //set the type
    shareIntent.setType("text/plain");
    //add a subject
    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
        getResources().getString(R.string.app_name));
    String packageName = getActivity().getPackageName();
    //build the body of the message to be shared
    String shareMessage =
        "Hey check this beautiful Music Player! \n\nhttps://play.google.com/store/apps/details?id="
            + packageName;
    //add the message
    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
    //start the chooser for sharing
    startActivity(Intent.createChooser(shareIntent, "Share with"));
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
    new AdmobBannerLoaded((ViewGroup) view);
    setupAdmobInterstial();
    if (BuildConfig.FLAVOR.equals("pro")) {
      removeAdsView.setVisibility(View.GONE);
    }
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            o -> {
              if (presenter != null && presenter.hasView()) {
                if (o instanceof DurationFilterEvent) {
                  int durationInSeconds = settingPreferences.filterDurationInSeconds();
                  durationSecondsView.setText(durationInSeconds + " sec");
                  analytics.logDurationFilter(durationInSeconds);
                  admobInterstitial.show();
                } else if (o instanceof PaletteEvent || o instanceof ThemeEvent
                    || o instanceof TransparencyChangedEvent) {
                  applyUiColors();
                } else if (o instanceof BlurRadiusEvent) {
                  admobInterstitial.show();
                  analytics.logBlurRadius(((BlurRadiusEvent) o).blurRadius());
                } else if (o instanceof BackgroundEvent) {
                  admobInterstitial.show();
                }
              } else {
                FirebaseCrashlytics.getInstance().recordException(
                    new NullPointerException(
                        String.format(
                            "class: %s presenter- %s hasView- %b",
                            SettingsFragment.class.getSimpleName(),
                            presenter, presenter != null && presenter.hasView()
                        )
                    )
                );
              }
            }
        );
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
    pauseOnHeadsetUnplugged.setChecked(settingPreferences.pauseOnHeadsetUnplugged());
    resumeOnHeadsetPlugged.setChecked(settingPreferences.resumeOnHeadsetPlugged());
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
    for (int count = 0; count < parentLayout.getChildCount() - 1; count++) {
      View viewOne = parentLayout.getChildAt(count);
      View viewTwo = parentLayout.getChildAt(count + 1);
      if (viewOne instanceof TextView) {
        if (viewTwo != null) {
          ((TextView) viewOne).setTextColor(smartTheme.titleText().intValue());
        }
        ((TextView) viewTwo).setTextColor(smartTheme.bodyText().intValue());
      } else if (viewOne instanceof ViewGroup) {
        deepChangeTextColor((ViewGroup) viewOne);
      }
    }
  }

  @OnClick(R.id.sleepTimerView) void onSleepTimerClick() {
    Calendar currentTime = Calendar.getInstance();
    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
    int minute = currentTime.get(Calendar.MINUTE);
    TimePickerDialog timePickerDialog;
    timePickerDialog =
        new TimePickerDialog(getActivity(), (timePicker, selectedHour, selectedMinute) -> {
          int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
          int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
          int diffHour = selectedHour - currentHour;
          int diffMinute = selectedMinute - currentMinute;
          setAlarmToStopApp(diffHour * 60 * 60 * 1000 + diffMinute * 60 * 1000);
        }, hour, minute, false);//Yes 24 hour time
    timePickerDialog.setTitle("Select Time");
    timePickerDialog.show();
  }

  private void setAlarmToStopApp(long scheduleAt) {
    PendingIntent pendingIntent =
        PendingIntent.getService(getActivity(), 0, new Intent(broadcastActionNames.ofStop()), 0);
    AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scheduleAt, pendingIntent);
  }

  @OnCheckedChanged(R.id.fullScreenCheckbox) void onFullScreenCheckedChange(boolean isChecked) {
    Window window = getActivity().getWindow();
    settingPreferences.saveFullScreen(isChecked);
    if (isChecked) {
      window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
      window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    } else {
      window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
    analytics.logFullScreen(isChecked);
  }

  @OnCheckedChanged(R.id.pauseOnHeadsetUnplugged) void onPauseOnHeadsetUnpluggedCheckedChange(
      boolean isChecked) {
    settingPreferences.pauseOnHeadsetUnplugged(isChecked);
  }

  @OnCheckedChanged(R.id.resumeOnHeadsetPlugged) void onResumeOnHeadsetPluggedCheckedChange(
      boolean isChecked) {
    settingPreferences.resumeOnHeadsetPlugged(isChecked);
  }

  private void setupAdmobInterstial() {
    admobInterstitial = new AdmobInterstitial(getActivity(),
        getResources().getString(R.string.kd_music_player_settings_interstitial),
        (AdListener) () -> {

        });
  }

  @OnCheckedChanged(R.id.flatThemeCheckbox) void onThemeCheckedChange(boolean isChecked) {
    settingPreferences.saveFlatTheme(isChecked);
    RxBus.getInstance().post(new ThemeEvent());
    analytics.logTheme(isChecked);
  }
}
