package com.kingbull.musicplayer.ui.addtoplaylist;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ViewFlipper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.storage.PlayListTable;
import com.kingbull.musicplayer.domain.storage.SqlMusic;
import com.kingbull.musicplayer.domain.storage.SqlPlayList;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */

public final class AddToPlayListDialogFragment extends DialogFragment
    implements AddToPlayList.View {
  AddToPlayList.Presenter presenter = new AddToPlayListPresenter();
  @BindView(R.id.createNewPlayListView) LinearLayout createNewPlaylistView;
  @BindView(R.id.playlistsView) LinearLayout playlistsView;
  @BindView(R.id.playlistNameView) EditText playlistNameView;
  @BindView(R.id.listView) ListView listView;
  @BindView(R.id.viewFlipper) ViewFlipper viewFlipper;
  @Inject PlayListTable playListTable;

  public static AddToPlayListDialogFragment newInstance(List<SqlMusic> musicList) {
    AddToPlayListDialogFragment frag = new AddToPlayListDialogFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList("music_list", (ArrayList<? extends Parcelable>) musicList);
    frag.setArguments(bundle);
    return frag;
  }

  @OnClick(R.id.createNewView) void onCreateNewClick() {
    presenter.onCreateNewClick();
  }

  @OnClick(R.id.cancelView) void onCancelClick() {
    showPlaylistsScreen();
  }

  @OnClick(R.id.doneView) void onDoneClick() {
    if (!TextUtils.isEmpty(playlistNameView.getText())) {
      List<SqlMusic> musics = getArguments().getParcelableArrayList("music_list");
      new SqlPlayList(playlistNameView.getText().toString(), musics).save();
      dismiss();
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_add_to_playlist, null);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    presenter.takeView(this);
    listView.setAdapter(new PlayListAdapter(getActivity(), playListTable.playlists()));
    //showPlaylistsScreen();
  }

  private void showPlaylistsScreen() {
    viewFlipper.setInAnimation(getActivity(), R.anim.slide_in_left);
    viewFlipper.setOutAnimation(getActivity(), R.anim.slide_out_right);
    viewFlipper.showPrevious();
  }

  @Override public void showCreatePlaylistScreen() {
    viewFlipper.setInAnimation(getActivity(), R.anim.slide_in_right);
    viewFlipper.setOutAnimation(getActivity(), R.anim.slide_out_left);
    viewFlipper.showNext();
  }
}
