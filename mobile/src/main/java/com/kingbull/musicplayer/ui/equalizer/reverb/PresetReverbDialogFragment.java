package com.kingbull.musicplayer.ui.equalizer.reverb;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.event.Preset;
import com.kingbull.musicplayer.player.Player;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */
public final class PresetReverbDialogFragment extends BaseDialogFragment
    implements PresetReverb.View {
  private final Reverb[] reverbs = {
      Reverb.LARGE_HALL, Reverb.LARGE_ROOM, Reverb.MEDIUM_HALL, Reverb.MEDIUM_ROOM,
      Reverb.SMALL_ROOM, Reverb.PLATE, Reverb.NONE,
  };
  @BindView(R.id.listView) ListView listView;
  @Inject Player player;
  private PresetReverb.Presenter presenter = new PresetReverbPresenter();

  public static PresetReverbDialogFragment newInstance() {
    PresetReverbDialogFragment frag = new PresetReverbDialogFragment();
    return frag;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_reverb_preset, container, false);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Display display = getDialog().getOwnerActivity().getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    setDialogHeight(size.y * 70 / 100);
  }

  private void setDialogHeight(int height) {
    ViewGroup.LayoutParams layoutParams = getView().getLayoutParams();
    layoutParams.height = height;
    getView().setLayoutParams(layoutParams);
  }

  @Override public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    presenter.takeView(this);
    final List<Reverb> reverbs = Arrays.asList(this.reverbs);
    listView.setAdapter(new PresetReverbAdapter(getActivity(), reverbs));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        player.useEffect(reverbs.get(position));
        new SettingPreferences().saveReverb(reverbs.get(position));
        RxBus.getInstance().post(Preset.Reverb(reverbs.get(position)));
        dismiss();
      }
    });
  }
}
