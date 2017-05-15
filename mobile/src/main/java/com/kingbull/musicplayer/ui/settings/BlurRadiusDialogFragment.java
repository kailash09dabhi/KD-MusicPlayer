package com.kingbull.musicplayer.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

  public static BlurRadiusDialogFragment newInstance() {
    BlurRadiusDialogFragment frag = new BlurRadiusDialogFragment();
    MusicPlayerApp.instance().component().inject(frag);
    return frag;
  }

  @OnClick(R.id.doneButton) void onDoneClick() {
    Observable.just(blurRadiusValueView.getText().toString()).filter(new Predicate<String>() {
      @Override public boolean test(String text) throws Exception {
        return !text.isEmpty();
      }
    }).map(new Function<String, Integer>() {
      @Override public Integer apply(String text) throws Exception {
        return Integer.parseInt(text);
      }
    }).filter(new Predicate<Integer>() {
      @Override public boolean test(Integer integer) throws Exception {
        if (integer < 255) {
          return true;
        } else {
          throw new IllegalArgumentException("Blur value is too large!");
        }
      }
    }).subscribe(new Consumer<Integer>() {
      @Override public void accept(Integer blurRadius) throws Exception {
        settingPreferences.blurRadius(blurRadius);
        RxBus.getInstance().post(new BlurRadiusEvent(blurRadius));
      }
    }, new Consumer<Throwable>() {
      @Override public void accept(Throwable throwable) throws Exception {
        Toast.makeText(getActivity(), "Blur value is too large!", Toast.LENGTH_SHORT).show();
      }
    });
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
    blurRadiusValueView.setText(String.valueOf(settingPreferences.blurRadius()));
  }
}
