/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.categories.all;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.main.categories.all.raymenu.RayMenu;
import com.kingbull.musicplayer.ui.songlist.SongsAdapter;
import java.util.List;

public final class AllSongsFragment extends BaseFragment<AllSongs.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, AllSongs.View {
  private static final int[] ITEM_DRAWABLES = {
      R.drawable.composer_button, R.drawable.composer_button_back, R.drawable.composer_button_queue,
      R.drawable.composer_icn_search, R.drawable.composer_button_shuffle,
      R.drawable.composer_button_multiselect
  };
  @BindView(R.id.totalSongCountView) TextView totalSongCountView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.ray_menu) RayMenu rayMenu;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_all_songs, null);
    ButterKnife.bind(this, view);
    setupView();
    return view;
  }

  private void setupView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    getLoaderManager().initLoader(0, null, this);
    final int itemCount = ITEM_DRAWABLES.length;
    for (int i = 0; i < itemCount; i++) {
      ImageView item = new ImageView(getActivity());
      item.setImageResource(ITEM_DRAWABLES[i]);
      final int position = i;
      rayMenu.addItem(item, new View.OnClickListener() {

        @Override public void onClick(View v) {
          Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
        }
      });// Add a menu item
    }
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AllSongsCursorLoader(getContext());
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    presenter.onAllSongsCursorLoadFinished(cursor);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override public void showAllSongs(List<Music> songs) {
    totalSongCountView.setText(String.valueOf(songs.size()));
    recyclerView.setAdapter(new SongsAdapter(songs));
  }

  @Override protected void onPresenterPrepared(AllSongs.Presenter presenter) {
    presenter.takeView(this);
  }

  @Override protected PresenterFactory<AllSongs.Presenter> presenterFactory() {
    return new PresenterFactory.AllSongs();
  }
}
