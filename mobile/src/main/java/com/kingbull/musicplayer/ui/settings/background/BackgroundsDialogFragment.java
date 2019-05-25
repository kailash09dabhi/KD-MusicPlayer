package com.kingbull.musicplayer.ui.settings.background;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.BuildConfig;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.ProLink;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.storage.preferences.Background;
import com.kingbull.musicplayer.event.BackgroundEvent;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */
public final class BackgroundsDialogFragment extends BaseDialogFragment {
  private static final int proStartIndex = 6;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.randomBackground) Button randomBackgroundButton;
  @BindView(R.id.titleView) TextView titleView;
  @Inject SharedPreferences sharedPreferences;

  public static BackgroundsDialogFragment newInstance() {
    BackgroundsDialogFragment frag = new BackgroundsDialogFragment();
    MusicPlayerApp.instance().component().inject(frag);
    return frag;
  }

  @OnClick(R.id.randomBackground) void randomBackgroundClick() {
    new Background.Smart(sharedPreferences).enableRandomMode();
    dismiss();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_background, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    recyclerView.setBackgroundColor(smartTheme.screen().intValue());
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    recyclerView.setAdapter(new BackgroundsAdapter(new OnBackgroundSelectionListener() {
          @Override public void onBackgroundSelection(int index) {
            if (BuildConfig.FLAVOR.equals("free")) {
              if (index >= proStartIndex) {
                new ProLink.PlayStore(getContext()).open();
              } else {
                makeSelection(index);
              }
            } else {
              makeSelection(index);
            }
          }
        }, proStartIndex)
    );
    titleView.setBackgroundColor(smartTheme.statusBar().intValue());
    randomBackgroundButton.setBackgroundColor(smartTheme.statusBar().intValue());
  }

  private void makeSelection(int index) {
    new Background.Smart(sharedPreferences).take(index);
    dismiss();
    RxBus.getInstance().post(new BackgroundEvent());
  }

  interface OnBackgroundSelectionListener {
    void onBackgroundSelection(int position);
  }
}
