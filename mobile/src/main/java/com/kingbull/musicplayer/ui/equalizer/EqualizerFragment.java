package com.kingbull.musicplayer.ui.equalizer;

import android.content.Context;
import android.media.AudioManager;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Virtualizer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.crashlytics.android.Crashlytics;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.event.Preset;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.StatusBarColor;
import com.kingbull.musicplayer.ui.equalizer.preset.PresetDialogFragment;
import com.kingbull.musicplayer.ui.equalizer.reverb.PresetReverbDialogFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 23 Nov, 2016
 */
public final class EqualizerFragment extends BaseFragment<Equalizer.Presenter>
    implements Equalizer.View {
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.newPresetButton) Button presetButton;
  @BindView(R.id.equalizerView) EqualizerView equalizerView;
  @BindView(R.id.effectButton) Button effectButton;
  @BindView(R.id.virtualizerRoundKnobLayout) RelativeLayout virtualizerRoundKnobLayout;
  @BindView(R.id.bassBoostLayout) RelativeLayout bassBoostRoundKnobLayout;
  @BindView(R.id.volumeLayout) RelativeLayout volumeRoundKnobLayout;
  @BindView(R.id.bottomButtonContainer) LinearLayout bottomButtonContainer;
  @Inject Player player;
  private AudioManager audioManager;
  private BassBoost bassBoost;
  private Virtualizer virtualizer;

  public static EqualizerFragment instance() {
    EqualizerFragment equalizerFragment = new EqualizerFragment();
    return equalizerFragment;
  }

  @OnClick(R.id.newPresetButton) void onNewPresetClick() {
    PresetDialogFragment.newInstance()
        .show(getActivity().getSupportFragmentManager(), PresetDialogFragment.class.getName());
  }

  @OnClick(R.id.effectButton) void onEffectClick() {
    PresetReverbDialogFragment.newInstance()
        .show(getActivity().getSupportFragmentManager(),
            PresetReverbDialogFragment.class.getName());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_equalizer, container, false);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    titleView.setText("Equalizer Preset".toUpperCase());
    setupRoundKnobButtonLayouts();
    com.kingbull.musicplayer.ui.base.Color color = flatTheme.screen();
    new StatusBarColor(color).applyOn(getActivity().getWindow());
    view.setBackground(color.toDrawable());
    bottomButtonContainer.setBackground(flatTheme.header().toDrawable());
    ((View) presetButton.getParent()).setBackground(flatTheme.header().toDrawable());
    return view;
  }

  private void setupRoundKnobButtonLayouts() {
    bassBoost = player.bassBoost();
    virtualizer = player.virtualizer();
    setupBassBoostButton();
    setupVolumeButton();
    setupVirtualizerButton();
    effectButton.setText(settingPreferences.reverb().name());
  }

  private void setupBassBoostButton() {
    final RoundKnobButton bassBoostButton = new RoundKnobButton(getActivity());
    bassBoostRoundKnobLayout.addView(bassBoostButton);
    //bassBoostButton.setRotorPercentage(bassBoost.getRoundedStrength() / 10);
    bassBoostButton.setRotorPercentage(100);
    bassBoostButton.addRotationListener(new RoundKnobButton.RoundKnobButtonListener() {
      @Override public void onStateChange(boolean newstate) {
      }

      @Override public void onRotate(int percentage) {
        if (bassBoost.getStrengthSupported()) {
          try {
            bassBoost.setStrength((short) (percentage * 10));
          } catch (Exception e) {
            Crashlytics.logException(
                new RuntimeException("bassboost percentage is " + percentage + " " + e, e));
          }
        }
      }
    });
  }

  private void setupVolumeButton() {
    final RoundKnobButton volumeButton = new RoundKnobButton(getActivity());
    volumeRoundKnobLayout.addView(volumeButton);
    final int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    volumeButton.addRotationListener(new RoundKnobButton.RoundKnobButtonListener() {
      @Override public void onStateChange(boolean newstate) {
      }

      @Override public void onRotate(int percentage) {
        int volume = (int) (maxVolume * percentage / 100.0);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
      }
    });
    //volumeButton.setRotorPercentage(
    //    audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / maxVolume * 100);
    volumeButton.setRotorPercentage(100);
  }

  private void setupVirtualizerButton() {
    final RoundKnobButton virtualizerButton = new RoundKnobButton(getActivity());
    virtualizerRoundKnobLayout.addView(virtualizerButton);
    //virtualizerButton.setRotorPercentage(virtualizer.getRoundedStrength() / 10);
    virtualizerButton.setRotorPercentage(100);
    virtualizerButton.addRotationListener(new RoundKnobButton.RoundKnobButtonListener() {
      @Override public void onStateChange(boolean newstate) {
      }

      @Override public void onRotate(int percentage) {
        if (virtualizer.getStrengthSupported()) {
          try {
            virtualizer.setStrength((short) (percentage * 10));
          } catch (Exception e) {
            Crashlytics.logException(
                new RuntimeException("virtualizer percentage is " + percentage + " " + e, e));
          }
        }
      }
    });
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .ofType(Preset.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Preset>() {
          @Override public void accept(Preset preset) {
            switch (preset.event()) {
              case Preset.Event.CLICK:
                presenter.onPresetSelected(preset.equalizerPreset());
                break;
              case Preset.Event.NEW:
                presenter.onNewPresetEvent(preset.presetName());
                break;
              case Preset.Event.REVERB:
                effectButton.setText(preset.reverb().name());
                break;
            }
          }
        });
  }

  @Override protected PresenterFactory<Equalizer.Presenter> presenterFactory() {
    return new PresenterFactory.Equalizer();
  }

  @Override protected void onPresenterPrepared(final Equalizer.Presenter presenter) {
    presenter.takeView(this);
    equalizerView.addOnBandValueChangeListener(new EqualizerView.OnBandValueChangeListener() {
      @Override public void onBandValueChange(short bandNumber, int percentageValue) {
        presenter.onBandValueChange(bandNumber, percentageValue);
      }
    });
  }

  @Override public void takeChosenPreset(final EqualizerPreset equalizerPreset) {
    presetButton.setText(equalizerPreset.name());
    equalizerView.post(new Runnable() {
      @Override public void run() {
        equalizerView.adjustToSelectedPreset(equalizerPreset);
      }
    });
  }

  @Override public void saveEqualizerPreset(String presetName) {
    equalizerView.asEqualizerPreset(presetName).save();
  }
}
