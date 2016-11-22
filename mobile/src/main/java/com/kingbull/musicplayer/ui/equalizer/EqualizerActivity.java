package com.kingbull.musicplayer.ui.equalizer;

import android.graphics.Point;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.BaseActivity;
import com.kingbull.musicplayer.ui.base.Presenter;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.equalizer.adapter.EqualizerSpinnerAdapter;
import java.util.ArrayList;

/**
 * Created by divyanshunegi on 11/9/15.
 */
public final class EqualizerActivity extends BaseActivity {

  @BindView(R.id.titleView) TextView titleView;
  private Equalizer equalizer;
@BindView(R.id.equalizerSpinner)
  Spinner equalizerSpinner;
  private EqualizerSpinnerAdapter adapter;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_equalizer);
    ButterKnife.bind(this);
    titleView.setText("Equalizer".toUpperCase());
    RelativeLayout verticalRoundKnobLayout =
        (RelativeLayout) findViewById(R.id.verticalRoundKnobLayout);
    RelativeLayout bassBoostRoundKnobLayout = (RelativeLayout) findViewById(R.id.bassBoostLayout);
    RelativeLayout volumeRoundKnobLayout = (RelativeLayout) findViewById(R.id.volumeLayout);
    Point localPoint = new Point();
    getWindowManager().getDefaultDisplay().getSize(localPoint);
    verticalRoundKnobLayout.getLayoutParams().width = localPoint.x / 3;
    verticalRoundKnobLayout.getLayoutParams().height = localPoint.x / 3;
    bassBoostRoundKnobLayout.getLayoutParams().width = localPoint.x / 3;
    bassBoostRoundKnobLayout.getLayoutParams().height = localPoint.x / 3;
    volumeRoundKnobLayout.getLayoutParams().width = localPoint.x / 3;
    volumeRoundKnobLayout.getLayoutParams().height = localPoint.x / 3;
    final RoundKnobButton button = new RoundKnobButton(this);
    verticalRoundKnobLayout.addView(button);
    button.setRotorPercentage(100);
    final RoundKnobButton button2 = new RoundKnobButton(this);
    bassBoostRoundKnobLayout.addView(button2);
    button2.setRotorPercentage(100);
    final RoundKnobButton button3 = new RoundKnobButton(this);
    volumeRoundKnobLayout.addView(button3);
    button3.setRotorPercentage(100);
    equalizer = new Equalizer(5, getIntent().getIntExtra("audio_session_id", 0));
    equalizer.setEnabled(true);
    equalizer = new Equalizer(5, getIntent().getIntExtra("audio_session_id", 0));
    equalizer.setEnabled(true);
    ArrayList<String> equalizerPresetNames = new ArrayList<String>();
    for (short i = 0; i < equalizer.getNumberOfPresets(); i++) {
      equalizerPresetNames.add(equalizer.getPresetName(i));
    }
    adapter = new EqualizerSpinnerAdapter(this, equalizerPresetNames);
    equalizerSpinner.setAdapter(adapter);
  }

  @Override protected void onPresenterPrepared(Presenter presenter) {
  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.MusicPlayer();
  }
}
