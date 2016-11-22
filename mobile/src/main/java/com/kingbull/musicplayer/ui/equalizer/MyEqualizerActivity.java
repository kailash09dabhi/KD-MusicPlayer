package com.kingbull.musicplayer.ui.equalizer;

import android.content.Intent;
import android.graphics.Point;
import android.media.audiofx.Equalizer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.equalizer.adapter.EqualizerSpinnerAdapter;
import java.util.ArrayList;

/**
 * Created by divyanshunegi on 11/9/15.
 */
public class MyEqualizerActivity extends AppCompatActivity {

  public static final String EQUALIZER_UPDATE = "com.muziko.audioplayer.equalizer.update";

  private Equalizer equalizer;
  private Spinner equalizerSpinner;
  private EqualizerSpinnerAdapter adapter;
  private TextView textHz1, textHz2, textHz3, textHz4, textHz5;
  private VerticalSeekBar seekBar1, seekBar2, seekBar3, seekBar4, seekBar5;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_equalizer);
    Singleton m_Inst = Singleton.getInstance();
    m_Inst.InitGUIFrame(this);
    RelativeLayout verticalRoundKnobLayout =
        (RelativeLayout) findViewById(R.id.verticalRoundKnobLayout);
    RelativeLayout bassBoostRoundKnobLayout = (RelativeLayout) findViewById(R.id.bassBoostLayout);


    RelativeLayout volumeRoundKnobLayout = (RelativeLayout) findViewById(R.id.volumeLayout);

    Point localPoint = new Point();
    getWindowManager().getDefaultDisplay().getSize(localPoint);
    verticalRoundKnobLayout.getLayoutParams().width = localPoint.x/3;
    verticalRoundKnobLayout.getLayoutParams().height =  localPoint.x/3;

    bassBoostRoundKnobLayout.getLayoutParams().width =localPoint.x/3;
    bassBoostRoundKnobLayout.getLayoutParams().height =  localPoint.x/3;

    volumeRoundKnobLayout.getLayoutParams().width =localPoint.x/3;
    volumeRoundKnobLayout.getLayoutParams().height =  localPoint.x/3;
    final RoundKnobButton button =
        new RoundKnobButton(this);
    verticalRoundKnobLayout.addView(button);
    button.setRotorPercentage(100);
    final RoundKnobButton button2 =
        new RoundKnobButton(this);
    bassBoostRoundKnobLayout.addView(button2);
    button2.setRotorPercentage(100);
    final RoundKnobButton button3 =
        new RoundKnobButton(this);
    volumeRoundKnobLayout.addView(button3);
    button3.setRotorPercentage(100);
    equalizerSpinner = (Spinner) findViewById(R.id.equalizerSpinner);
    textHz1 = (TextView) findViewById(R.id.slider1Layout);
    textHz2 = (TextView) findViewById(R.id.slider2Layout);
    textHz3 = (TextView) findViewById(R.id.slider3Layout);
    textHz4 = (TextView) findViewById(R.id.slider4Layout);
    textHz5 = (TextView) findViewById(R.id.slider5Layout);
    seekBar1 = (VerticalSeekBar) findViewById(R.id.mySeekBar1);
    seekBar2 = (VerticalSeekBar) findViewById(R.id.mySeekBar2);
    seekBar3 = (VerticalSeekBar) findViewById(R.id.mySeekBar3);
    seekBar4 = (VerticalSeekBar) findViewById(R.id.mySeekBar4);
    seekBar5 = (VerticalSeekBar) findViewById(R.id.mySeekBar5);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
    }
    equalizer = new Equalizer(5, getIntent().getIntExtra("audio_session_id", 0));
    equalizer.setEnabled(true);
    ArrayList<String> equalizerPresetNames = new ArrayList<String>();
    for (short i = 0; i < equalizer.getNumberOfPresets(); i++) {
      equalizerPresetNames.add(equalizer.getPresetName(i));
    }
    adapter = new EqualizerSpinnerAdapter(this, equalizerPresetNames);
    equalizerSpinner.setAdapter(adapter);
    seekBarChangeListners();
    setupSeekBars();
    equalizerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //first list item selected by default and sets the preset accordingly
        equalizer.usePreset((short) position);
        //                get the number of frequency bands for this equalizer engine
        short numberFrequencyBands = equalizer.getNumberOfBands();
        //                get the lower gain setting for this equalizer band
        final short lowerEqualizerBandLevel = equalizer.getBandLevelRange()[0];
        //                set seekBar indicators according to selected preset
        for (short i = 0; i < numberFrequencyBands; i++) {
          short equalizerBandIndex = i;
          //                    get current gain setting for this equalizer band
          //                    set the progress indicator of this seekBar to indicate the current gain value
          switch (i) {
            case 0:
              seekBar1.setProgress(
                  equalizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
              break;
            case 1:
              seekBar2.setProgress(
                  equalizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
              break;
            case 2:
              seekBar3.setProgress(
                  equalizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
              break;
            case 3:
              seekBar4.setProgress(
                  equalizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
              break;
            case 4:
              seekBar5.setProgress(
                  equalizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
              break;
          }
        }
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {
      }
    });
  }

  private void setupSeekBars() {
    short numberOfFrequencyBands = equalizer.getNumberOfBands();
    for (int i = 0; i < numberOfFrequencyBands; i++) {
      String hz =
          equalizer.getCenterFreq((short) i) / 1000 > 1000 ? (equalizer.getCenterFreq((short) i)
              / 1000) / 1000 + "KHz" : (equalizer.getCenterFreq((short) i) / 1000) + "Hz";
      switch (i) {
        case 0:
          textHz1.setText(hz);
          seekBar1.setMax(equalizer.getBandLevelRange()[1] - equalizer.getBandLevelRange()[0]);
          seekBar1.setProgress(equalizer.getBandLevel((short) i));
          break;
        case 1:
          textHz2.setText(hz);
          seekBar2.setMax(equalizer.getBandLevelRange()[1] - equalizer.getBandLevelRange()[0]);
          seekBar2.setProgress(equalizer.getBandLevel((short) i));
          break;
        case 2:
          textHz3.setText(hz);
          seekBar3.setMax(equalizer.getBandLevelRange()[1] - equalizer.getBandLevelRange()[0]);
          seekBar3.setProgress(equalizer.getBandLevel((short) i));
          break;
        case 3:
          textHz4.setText(hz);
          seekBar4.setMax(equalizer.getBandLevelRange()[1] - equalizer.getBandLevelRange()[0]);
          seekBar4.setProgress(equalizer.getBandLevel((short) i));
          break;
        case 4:
          textHz5.setText(hz);
          seekBar5.setMax(equalizer.getBandLevelRange()[1] - equalizer.getBandLevelRange()[0]);
          seekBar5.setProgress(equalizer.getBandLevel((short) i));
          break;
      }
    }
  }

  private void seekBarChangeListners() {
    final short lowerEqualizerBandLevel = equalizer.getBandLevelRange()[0];
    seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //                eq.setBandLevel((short) 0, (short) (progress + eq.getBandLevelRange()[0]));
        Intent in = new Intent(EQUALIZER_UPDATE);
        sendBroadcast(in);
        equalizer.setBandLevel((short) 0, (short) (progress + lowerEqualizerBandLevel));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
    seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //                eq.setBandLevel((short)1,(short)(progress+eq.getBandLevelRange()[0]));
        Intent in = new Intent(EQUALIZER_UPDATE);
        sendBroadcast(in);
        equalizer.setBandLevel((short) 1, (short) (progress + lowerEqualizerBandLevel));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
    seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Intent in = new Intent(EQUALIZER_UPDATE);
        sendBroadcast(in);
        equalizer.setBandLevel((short) 2, (short) (progress + lowerEqualizerBandLevel));
        //                eq.setBandLevel((short)2,(short)(progress+eq.getBandLevelRange()[0]));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
    seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Intent in = new Intent(EQUALIZER_UPDATE);
        equalizer.setBandLevel((short) 3, (short) (progress + lowerEqualizerBandLevel));
        sendBroadcast(in);
        //eq.setBandLevel((short)3,(short)(progress+eq.getBandLevelRange()[0]));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
    seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Intent in = new Intent(EQUALIZER_UPDATE);
        sendBroadcast(in);
        equalizer.setBandLevel((short) 4, (short) (progress + lowerEqualizerBandLevel));
        //eq.setBandLevel((short)4,(short)(progress+eq.getBandLevelRange()[0]));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
  }
}
