package com.kingbull.musicplayer.ui.addtoplaylist;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlMusic;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaStatTable;
import com.kingbull.musicplayer.domain.storage.sqlite.table.PlayListTable;
import com.kingbull.musicplayer.event.PlaylistCreatedEvent;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */

public final class AddToPlayListDialogFragment extends BaseDialogFragment
    implements AddToPlayList.View {
  AddToPlayList.Presenter presenter = new AddToPlayListPresenter();
  @BindView(R.id.createNewPlayListView) LinearLayout createNewPlaylistView;
  @BindView(R.id.playlistsView) LinearLayout playlistsView;
  @BindView(R.id.playlistNameView) EditText playlistNameView;
  @BindView(R.id.listView) ListView listView;
  @BindView(R.id.viewFlipper) ViewFlipper viewFlipper;
  @Inject MediaStatTable mediaStatTable;
  List<SqlMusic> musics;
  List<PlayList> playLists;

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

  private void showPlaylistsScreen() {
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
    if (!TextUtils.isEmpty(playlistNameView.getText())) {
      List<SqlMusic> musics = getArguments().getParcelableArrayList("music_list");
      PlayList.Smart playList = (PlayList.Smart) new PlayListTable().addNewPlaylist(
          playlistNameView.getText().toString());
      playList.addAll(musics);
      RxBus.getInstance().post(new PlaylistCreatedEvent(playList));
      dismiss();
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_add_to_playlist, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    musics = getArguments().getParcelableArrayList("music_list");
    playLists = new PlayListTable().allPlaylists();
    presenter.takeView(this);
    listView.setAdapter(new PlayListAdapter(getActivity(), playLists));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((PlayList.Smart) playLists.get(position)).addAll(musics);
        dismiss();
      }
    });
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Display display = getDialog().getOwnerActivity().getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    setDialogHeight(size.y * 70 / 100);
  }

  @Override public void showCreatePlaylistScreen() {
    viewFlipper.setInAnimation(getActivity(), R.anim.slide_in_right);
    viewFlipper.setOutAnimation(getActivity(), R.anim.slide_out_left);
    viewFlipper.showNext();
    setDialogHeight(createNewPlaylistView.getMeasuredHeight());
  }
}
