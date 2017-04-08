package com.kingbull.musicplayer.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;

/**
 * @author Kailash Dabhi
 * @date 12/22/2016.
 */
public abstract class BaseDialogFragment extends DialogFragment {
  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    getDialog().getWindow().setBackgroundDrawable(new UiColors().dialog().asDrawable());
  }
}
