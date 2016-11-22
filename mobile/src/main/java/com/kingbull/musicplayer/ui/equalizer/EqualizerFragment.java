/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.equalizer;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import java.util.ArrayList;

public final class EqualizerFragment extends BaseFragment<Equalizer.Presenter>
    implements Equalizer.View {
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.equalizerSpinner) Spinner equalizerSpinner;
  @BindView(R.id.equalizerView) EqualizerView equalizerView;
  @BindView(R.id.verticalRoundKnobLayout) RelativeLayout verticalRoundKnobLayout;
  @BindView(R.id.bassBoostLayout) RelativeLayout bassBoostRoundKnobLayout;
  @BindView(R.id.volumeLayout) RelativeLayout volumeRoundKnobLayout;
  private android.media.audiofx.Equalizer equalizer;
  private EqualizerSpinnerAdapter adapter;

  public static EqualizerFragment instance(int audioSessionId) {
    EqualizerFragment equalizerFragment = new EqualizerFragment();
    Bundle args = new Bundle();
    args.putInt("audio_session_id", audioSessionId);
    equalizerFragment.setArguments(args);
    return equalizerFragment;
  }

  @OnItemSelected(R.id.equalizerSpinner) void onPresetSelected(int position) {
    presenter.onPresetSelected(position);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_equalizer, null);
    ButterKnife.bind(this, view);
    titleView.setText("Equalizer".toUpperCase());
    setupRoundKnobButton();
    equalizer = new android.media.audiofx.Equalizer(5, getArguments().getInt("audio_session_id"));
    equalizer.setEnabled(true);
    ArrayList<String> equalizerPresetNames = new ArrayList<>();
    for (short i = 0; i < equalizer.getNumberOfPresets(); i++) {
      equalizerPresetNames.add(equalizer.getPresetName(i));
    }
    adapter = new EqualizerSpinnerAdapter(getActivity(), equalizerPresetNames);
    equalizerSpinner.setAdapter(adapter);
    equalizerView.addOnBandValueChangeListener(new EqualizerView.OnBandValueChangeListener() {
      @Override public void onBandValueChange(short bandNumber, int percentageValue) {
        presenter.onBandValueChange(bandNumber, percentageValue);
      }
    });
    return view;
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
    button2.setRotorPercentage(100);
    final RoundKnobButton button3 = new RoundKnobButton(getActivity());
    volumeRoundKnobLayout.addView(button3);
    button3.setRotorPercentage(100);
  }

  @Override protected void onPresenterPrepared(Equalizer.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override protected PresenterFactory<Equalizer.Presenter> presenterFactory() {
    return new PresenterFactory.Equalizer();
  }

  @Override public void updateEqualizer(int position) {
    equalizer.usePreset((short) position);
    equalizerView.adjustToSelectedPreset(equalizer);
  }

  @Override public void updateBand(short bandNumber, int percentageValue) {
    Log.e("onBandValueChange", "band " + bandNumber + " percentage " + percentageValue);
    final short lowerEqualizerBandLevel = (short) equalizer.getBandLevelRange()[0];
    final short upperEqualizerBandLevel = (short) equalizer.getBandLevelRange()[1];
    final short maxBandLevel = (short) (upperEqualizerBandLevel - lowerEqualizerBandLevel);
    equalizer.setBandLevel(bandNumber,
        (short) (maxBandLevel * percentageValue / 100.0 + lowerEqualizerBandLevel));
  }
}
