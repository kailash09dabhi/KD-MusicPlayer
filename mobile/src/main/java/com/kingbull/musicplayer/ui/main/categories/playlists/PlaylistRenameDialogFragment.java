package com.kingbull.musicplayer.ui.main.categories.playlists;

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
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.event.PlaylistRenameEvent;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */
public final class PlaylistRenameDialogFragment extends BaseDialogFragment {
  @BindView(R.id.playlistNameView) EditText playlistNameView;

  public static PlaylistRenameDialogFragment newInstance(PlayList.Smart playList) {
    PlaylistRenameDialogFragment fragment = new PlaylistRenameDialogFragment();
    MusicPlayerApp.instance().component().inject(fragment);
    Bundle bundle = new Bundle();
    bundle.putParcelable("playlist", playList);
    fragment.setArguments(bundle);
    return fragment;
  }

  @OnClick(R.id.doneButton) void onDoneClick() {
    if (!TextUtils.isEmpty(playlistNameView.getText())) {
      PlayList.Smart playList = getArguments().getParcelable("playlist");
      RxBus.getInstance()
          .post(new PlaylistRenameEvent(playList, playlistNameView.getText().toString()));
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
    return inflater.inflate(R.layout.dialog_playlist_rename, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    getDialog().getWindow()
        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    playlistNameView.setHintTextColor(smartTheme.bodyText().intValue());
  }
}
