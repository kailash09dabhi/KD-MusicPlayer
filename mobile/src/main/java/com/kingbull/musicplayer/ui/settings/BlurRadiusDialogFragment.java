package com.kingbull.musicplayer.ui.settings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.BlurRadiusEvent;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */
public final class BlurRadiusDialogFragment extends BaseDialogFragment {
  @BindView(R.id.blurRadiusValue) EditText blurRadiusValueView;
  @BindView(R.id.doneButton) ImageView doneButton;

  public static BlurRadiusDialogFragment newInstance() {
    BlurRadiusDialogFragment frag = new BlurRadiusDialogFragment();
    MusicPlayerApp.instance().component().inject(frag);
    return frag;
  }

  @OnEditorAction(R.id.blurRadiusValue) boolean onBlurRadiusValueFilterEditorAction(int id,
      KeyEvent key) {
    if (id == R.id.apply || id == EditorInfo.IME_NULL) {
      doneButton.performClick();
      return true;
    }
    return false;
  }

  @OnClick(R.id.doneButton) void onDoneClick() {
    Observable.just(blurRadiusValueView.getText().toString()).filter(text -> !text.isEmpty()).map(text -> Integer.parseInt(text)).filter(integer -> {
      if (integer < 255) {
        return true;
      } else {
        throw new IllegalArgumentException("Blur value is too large!");
      }
    }).subscribe(blurRadius -> {
      settingPreferences.blurRadius(blurRadius);
      RxBus.getInstance().post(new BlurRadiusEvent(blurRadius));
    }, throwable -> Toast.makeText(getActivity(), "Blur value is too large!", Toast.LENGTH_SHORT).show());
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
    getDialog().getWindow()
        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    blurRadiusValueView.setText(String.valueOf(settingPreferences.blurRadius()));
    blurRadiusValueView.setSelection(blurRadiusValueView.length());
  }
}
