package com.kingbull.musicplayer.ui.equalizer.preset;

import android.graphics.Point;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ViewFlipper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.domain.storage.sqlite.table.EqualizerPresetTable;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;
import com.kingbull.musicplayer.ui.equalizer.AudioFxEqualizerPreset;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */
public final class PresetDialogFragment extends BaseDialogFragment implements Preset.View {
  @BindView(R.id.createNewPlayListView) LinearLayout createNewPresetView;
  @BindView(R.id.playlistsView) LinearLayout presetsView;
  @BindView(R.id.playlistNameView) EditText presetNameView;
  @BindView(R.id.listView) ListView listView;
  @BindView(R.id.viewFlipper) ViewFlipper viewFlipper;
  @Inject EqualizerPresetTable equalizerPresetTable;
  @Inject Player player;
  private Preset.Presenter presenter = new PresetPresenter();

  public static PresetDialogFragment newInstance() {
    PresetDialogFragment frag = new PresetDialogFragment();
    MusicPlayerApp.instance().component().inject(frag);
    return frag;
  }

  @OnClick(R.id.createNewView) void onCreateNewClick() {
    presenter.onCreateNewClick();
  }

  @OnClick(R.id.cancelView) void onCancelClick() {
    showPresetsScreen();
  }

  private void showPresetsScreen() {
    Log.e("viewFlipper", String.valueOf(viewFlipper.getHeight()));
    Log.e("createNewPresetView", String.valueOf(createNewPresetView.getMeasuredHeight()));
    Log.e("presetsView", String.valueOf(presetsView.getMeasuredHeight()));
    viewFlipper.setInAnimation(getActivity(), R.anim.slide_in_left);
    viewFlipper.setOutAnimation(getActivity(), R.anim.slide_out_right);
    viewFlipper.showPrevious();
    Display display = getDialog().getOwnerActivity().getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    setDialogHeight(size.y * 70 / 100);
  }

  void setDialogHeight(int height) {
    ViewGroup.LayoutParams layoutParams = getView().getLayoutParams();
    layoutParams.height = height;
    getView().setLayoutParams(layoutParams);
  }

  @OnClick(R.id.doneView) void onDoneClick() {
    if (!TextUtils.isEmpty(presetNameView.getText())) {
      RxBus.getInstance()
          .post(com.kingbull.musicplayer.event.Preset.New(presetNameView.getText().toString()));
      dismiss();
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_equalizer_preset, container, false);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Display display = getDialog().getOwnerActivity().getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    setDialogHeight(size.y * 70 / 100);
  }

  @Override public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    final List<EqualizerPreset> presetList = new ArrayList<>();
    presetList.addAll(equalizerPresetTable.allPresets());
    presetList.addAll(systemPresets());
    presenter.takeView(this);
    listView.setAdapter(new PresetAdapter(getActivity(), presetList));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        RxBus.getInstance()
            .post(com.kingbull.musicplayer.event.Preset.Click(presetList.get(position)));
        dismiss();
      }
    });
  }

  private List<EqualizerPreset> systemPresets() {
    Equalizer equalizer = player.equalizer();
    ArrayList<EqualizerPreset> equalizerPresets = new ArrayList<>();
    for (short i = 0; i < equalizer.getNumberOfPresets(); i++) {
      equalizerPresets.add(new AudioFxEqualizerPreset(equalizer, i));
    }
    return equalizerPresets;
  }

  @Override public void showCreatePresetScreen() {
    viewFlipper.setInAnimation(getActivity(), R.anim.slide_in_right);
    viewFlipper.setOutAnimation(getActivity(), R.anim.slide_out_left);
    viewFlipper.showNext();
    setDialogHeight(createNewPresetView.getMeasuredHeight());
  }
}
