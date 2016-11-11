/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.main.songgroup.album;

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

public final class AlbumFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>, Album.View {
  Album.Presenter presenter = new AlbumPresenter();
  private RecyclerView recyclerView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_genres, null);
    setupView(view);
    presenter.takeView(this);
    return view;
  }

  private void setupView(View v) {
    recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_grid);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    getLoaderManager().initLoader(0, null, this);
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AlbumCursorLoader(getContext());
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    presenter.onAlbumCursorLoadFinished(cursor);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
    // Empty
  }

  @Override public void showAlbums(List<AlbumItem> songs) {
    recyclerView.setAdapter(new AlbumAdapter(songs));
  }
}
