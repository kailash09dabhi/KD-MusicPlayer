package com.kingbull.musicplayer.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.DurationFilterEvent;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */
public final class DurationFilterDialogFragment extends BaseDialogFragment {
  @BindView(R.id.durationSecondsView) EditText durationSecondsView;

  public static DurationFilterDialogFragment newInstance() {
    DurationFilterDialogFragment frag = new DurationFilterDialogFragment();
    MusicPlayerApp.instance().component().inject(frag);
    return frag;
  }

  @OnClick(R.id.doneButton) void onDoneClick() {
    if (!TextUtils.isEmpty(durationSecondsView.getText())) {
      settingPreferences.saveFilterDurationInSeconds(
          Integer.parseInt(durationSecondsView.getText().toString()));
      RxBus.getInstance()
          .post(new DurationFilterEvent(Long.parseLong(durationSecondsView.getText().toString())));
    } else {
    }
    dismiss();
  }

  @OnClick(R.id.closeButton) void onCloseClick() {
    dismiss();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_duration_filter, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    getDialog().getWindow()
        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    durationSecondsView.setText(String.valueOf(settingPreferences.filterDurationInSeconds()));
  }
}
