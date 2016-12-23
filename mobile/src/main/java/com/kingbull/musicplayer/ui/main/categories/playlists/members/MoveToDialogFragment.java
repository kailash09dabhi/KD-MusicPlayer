package com.kingbull.musicplayer.ui.main.categories.playlists.members;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.domain.PlayList;
import com.kingbull.musicplayer.domain.storage.sqlite.table.AndroidPlayListTable;
import com.kingbull.musicplayer.event.MovedToPlaylistEvent;
import com.kingbull.musicplayer.ui.addtoplaylist.PlayListAdapter;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */

public final class MoveToDialogFragment extends BaseDialogFragment {
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.listView) ListView listView;

  public static MoveToDialogFragment newInstance(PlayList playList, Music music, int position) {
    MoveToDialogFragment frag = new MoveToDialogFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelable("playlist", (Parcelable) playList);
    bundle.putParcelable("music", (Parcelable) music);
    bundle.putInt("position", position);
    frag.setArguments(bundle);
    return frag;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_reverb_preset, null);
  }

  @Override public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    titleView.setText("Move to");
    final PlayList.Smart sourcePlayList = getArguments().getParcelable("playlist");
    final List<PlayList> allPlayLists = new AndroidPlayListTable().allPlaylists();
    allPlayLists.remove(sourcePlayList);
    listView.setAdapter(new PlayListAdapter(getActivity(), allPlayLists));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        Music music = getArguments().getParcelable("music");
        List<Music> musics = new ArrayList<>(1);
        musics.add(music);
        sourcePlayList.remove(music);
        PlayList.Smart destinationPlaylist = (PlayList.Smart) allPlayLists.get(position);
        destinationPlaylist.addAll(musics);
        RxBus.getInstance()
            .post(new MovedToPlaylistEvent(getArguments().getInt("position"),
                destinationPlaylist.name()));
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

  void setDialogHeight(int height) {
    ViewGroup.LayoutParams layoutParams = getView().getLayoutParams();
    layoutParams.height = height;
    getView().setLayoutParams(layoutParams);
  }
}
