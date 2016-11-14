/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.categories.playlists;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kingbull.musicplayer.R;
import java.util.List;

public final class PlayListsFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>, PlayLists.View {
  PlayLists.Presenter presenter = new PlayListsPresenter();
  private RecyclerView recyclerView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_genres, null);
    setupView(view);
    presenter.takeView(this);
    return view;
  }

  private void setupView(View v) {
    recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    getLoaderManager().initLoader(0, null, this);
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AllSongsCursorLoader(getContext());
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    presenter.onAllSongsCursorLoadFinished(cursor);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
    // Empty
  }

  @Override public void showAllPlaylist(List<PlayList> playLists) {
    recyclerView.setAdapter(new PlayListsAdapter(playLists));
  }
}
