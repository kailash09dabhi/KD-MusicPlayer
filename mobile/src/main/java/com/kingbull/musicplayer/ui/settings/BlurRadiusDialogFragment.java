package com.kingbull.musicplayer.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.event.BlurRadiusEvent;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */
public final class BlurRadiusDialogFragment extends BaseDialogFragment {
  @BindView(R.id.blurRadiusValue) EditText blurRadiusValueView;

  @OnClick(R.id.doneButton) void onDoneClick() {
    if (!TextUtils.isEmpty(blurRadiusValueView.getText())) {
      new SettingPreferences().blurRadius(
          Integer.parseInt(blurRadiusValueView.getText().toString()));
      RxBus.getInstance().post(new BlurRadiusEvent());
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
    return inflater.inflate(R.layout.dialog_blur_radius, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    blurRadiusValueView.setText(String.valueOf(new SettingPreferences().blurRadius()));
  }
}
