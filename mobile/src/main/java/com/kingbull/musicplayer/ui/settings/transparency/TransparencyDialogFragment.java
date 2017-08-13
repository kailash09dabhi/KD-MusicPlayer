package com.kingbull.musicplayer.ui.settings.transparency;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.storage.preferences.Transparency;
import com.kingbull.musicplayer.domain.storage.preferences.Transparency.Header;
import com.kingbull.musicplayer.event.TransparencyChangedEvent;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;
import javax.inject.Inject;

/**
 * Represents Dialog for transparency config.
 * @author Kailash Dabhi
 * @date 11/27/2016
 */
public final class TransparencyDialogFragment extends BaseDialogFragment {
  @BindView(R.id.doneButton)
  ImageView doneButton;
  @BindView(R.id.headerBar)
  SeekBar headerBar;
  @BindView(R.id.bodyBar)
  SeekBar bodyBar;
  @Inject
  SharedPreferences sharedPreferences;

  public static TransparencyDialogFragment newInstance() {
    TransparencyDialogFragment frag = new TransparencyDialogFragment();
    MusicPlayerApp.instance().component().inject(frag);
    return frag;
  }

  @OnClick(R.id.doneButton) void onDoneClick() {
//    if (!TextUtils.isEmpty(durationSecondsView.getText())) {
//      settingPreferences
//          .transparency(Float.parseFloat(durationSecondsView.getText().toString()) / 100.0f);
    RxBus.getInstance()
        .post(new TransparencyChangedEvent());
//    } else {
//    }
    dismiss();
  }

  @OnClick(R.id.closeButton) void onCloseClick() {
    dismiss();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_transparency, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);

    Transparency headerTransparency = new Header(sharedPreferences);

    headerBar.setProgress((int) (headerTransparency.value() * 100.f));
    headerBar.setOnSeekBarChangeListener(
        onSeekbarChangeListener(headerTransparency)
    );
    Transparency bodyTransparency = new Transparency.Body(sharedPreferences);
    bodyBar.setProgress((int) (bodyTransparency.value() * 100.0f));
    bodyBar.setOnSeekBarChangeListener(
        onSeekbarChangeListener(bodyTransparency)
    );
  }

  @NonNull private OnSeekBarChangeListener onSeekbarChangeListener(
      final Transparency transparency) {
    return new OnSeekBarChangeListener() {

      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
        transparency.apply(seekBar.getProgress() / 100.0f);
        RxBus.getInstance().post(new TransparencyChangedEvent());
      }
    };
  }
}
