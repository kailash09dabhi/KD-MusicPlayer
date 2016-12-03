/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.equalizer;

import android.graphics.Point;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.event.Preset;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.equalizer.preset.PresetDialogFragment;
import com.kingbull.musicplayer.ui.equalizer.reverb.PresetReverbDialogFragment;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public final class EqualizerFragment extends BaseFragment<Equalizer.Presenter>
    implements Equalizer.View {
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.newPresetButton) Button presetButton;
  @BindView(R.id.equalizerView) EqualizerView equalizerView;
  @BindView(R.id.effectButton) Button effectButton;
  @BindView(R.id.verticalRoundKnobLayout) RelativeLayout verticalRoundKnobLayout;
  @BindView(R.id.bassBoostLayout) RelativeLayout bassBoostRoundKnobLayout;
  @BindView(R.id.volumeLayout) RelativeLayout volumeRoundKnobLayout;
  CompositeSubscription compositeSubscription = new CompositeSubscription();
  @Inject Player player;
  private BassBoost bassBoost;

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
    View view = inflater.inflate(R.layout.fragment_equalizer, null);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    titleView.setText("Equalizer Preset".toUpperCase());
    setupRoundKnobButton();
    compositeSubscription.add(RxBus.getInstance()
        .toObservable()
        .ofType(Preset.class)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(new Action1<Preset>() {
          @Override public void call(Preset preset) {
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
        })
        .subscribe(RxBus.defaultSubscriber()));
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (compositeSubscription != null) {
      compositeSubscription.clear();
    }
  }

  private void setupRoundKnobButton() {
    Point localPoint = new Point();
    getActivity().getWindowManager().getDefaultDisplay().getSize(localPoint);
    verticalRoundKnobLayout.getLayoutParams().width = localPoint.x / 3;
    verticalRoundKnobLayout.getLayoutParams().height = localPoint.x / 3;
    bassBoostRoundKnobLayout.getLayoutParams().width = localPoint.x / 3;
    bassBoostRoundKnobLayout.getLayoutParams().height = localPoint.x / 3;
    volumeRoundKnobLayout.getLayoutParams().width = localPoint.x / 3;
    volumeRoundKnobLayout.getLayoutParams().height = localPoint.x / 3;
    final RoundKnobButton button = new RoundKnobButton(getActivity());
    verticalRoundKnobLayout.addView(button);
    button.setRotorPercentage(100);
    final RoundKnobButton button2 = new RoundKnobButton(getActivity());
    bassBoostRoundKnobLayout.addView(button2);
    bassBoost = player.bassBoost();
    button2.addRotationListener(new RoundKnobButton.RoundKnobButtonListener() {
      @Override public void onStateChange(boolean newstate) {
      }

      @Override public void onRotate(int percentage) {
        if (bassBoost.getStrengthSupported()) {
          bassBoost.setStrength((short) (percentage*10));
        }
      }
    });
    button2.setRotorPercentage(100);
    final RoundKnobButton button3 = new RoundKnobButton(getActivity());
    volumeRoundKnobLayout.addView(button3);
    button3.setRotorPercentage(100);
  }

  @Override protected void onPresenterPrepared(final Equalizer.Presenter presenter) {
    presenter.takeView(this);
    equalizerView.addOnBandValueChangeListener(new EqualizerView.OnBandValueChangeListener() {
      @Override public void onBandValueChange(short bandNumber, int percentageValue) {
        presenter.onBandValueChange(bandNumber, percentageValue);
      }
    });
  }

  @Override protected PresenterFactory<Equalizer.Presenter> presenterFactory() {
    return new PresenterFactory.Equalizer();
  }

  @Override public void takeChosenPreset(final EqualizerPreset equalizerPreset) {
    //equalizerSpinner.setSelection(position);
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
