/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.equalizer;

import android.content.Context;
import android.graphics.Point;
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
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.event.Preset;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.StatusBarColor;
import com.kingbull.musicplayer.ui.equalizer.preset.PresetDialogFragment;
import com.kingbull.musicplayer.ui.equalizer.reverb.PresetReverbDialogFragment;
import com.kingbull.musicplayer.ui.equalizer.reverb.Reverb;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import javax.inject.Inject;

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
    setupRoundKnobButton();
    com.kingbull.musicplayer.ui.base.Color color = flatTheme.screen();
    new StatusBarColor(color).applyOn(getActivity().getWindow());
    view.setBackground(color.toDrawable());
    bottomButtonContainer.setBackground(flatTheme.header().toDrawable());
    ((View) presetButton.getParent()).setBackground(flatTheme.header().toDrawable());
    return view;
  }

  private void setupRoundKnobButton() {
    Point localPoint = new Point();
    getActivity().getWindowManager().getDefaultDisplay().getSize(localPoint);
    virtualizerRoundKnobLayout.getLayoutParams().width = localPoint.x / 3;
    virtualizerRoundKnobLayout.getLayoutParams().height = localPoint.x / 3;
    bassBoostRoundKnobLayout.getLayoutParams().width = localPoint.x / 3;
    bassBoostRoundKnobLayout.getLayoutParams().height = localPoint.x / 3;
    volumeRoundKnobLayout.getLayoutParams().width = localPoint.x / 3;
    volumeRoundKnobLayout.getLayoutParams().height = localPoint.x / 3;
    final RoundKnobButton bassBoostButton = new RoundKnobButton(getActivity());
    bassBoostRoundKnobLayout.addView(bassBoostButton);
    bassBoostButton.setRotorPercentage(100);
    bassBoostButton.addRotationListener(new RoundKnobButton.RoundKnobButtonListener() {
      @Override public void onStateChange(boolean newstate) {
      }

      @Override public void onRotate(int percentage) {
        if (bassBoost.getStrengthSupported()) {
          bassBoost.setStrength((short) (percentage * 10));
        }
      }
    });
    final RoundKnobButton volumeButton = new RoundKnobButton(getActivity());
    volumeRoundKnobLayout.addView(volumeButton);
    bassBoost = player.bassBoost();
    virtualizer = player.virtualizer();
    final int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    volumeButton.addRotationListener(new RoundKnobButton.RoundKnobButtonListener() {
      @Override public void onStateChange(boolean newstate) {
      }

      @Override public void onRotate(int percentage) {
        int volume = (int) (maxVolume * percentage / 100.0);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
      }
    });
    volumeButton.setRotorPercentage(100);
    final RoundKnobButton virtualizerButton = new RoundKnobButton(getActivity());
    virtualizerRoundKnobLayout.addView(virtualizerButton);
    virtualizerButton.setRotorPercentage(100);
    virtualizerButton.addRotationListener(new RoundKnobButton.RoundKnobButtonListener() {
      @Override public void onStateChange(boolean newstate) {
      }

      @Override public void onRotate(int percentage) {
        if (virtualizer.getStrengthSupported()) {
          virtualizer.setStrength((short) (percentage * 10));
        }
      }
    });
    Reverb reverb = new SettingPreferences().reverb();
    effectButton.setText(reverb.name());
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
