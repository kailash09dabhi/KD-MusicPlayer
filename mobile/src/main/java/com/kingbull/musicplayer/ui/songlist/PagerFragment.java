/*
 * This is the source code of DMPLayer for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry, 2015.
 */
package com.kingbull.musicplayer.ui.songlist;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.utils.AlbumUtils;

public final class PagerFragment extends Fragment {
  @BindView(R.id.image) ImageView imageView;
  @BindView(R.id.label) TextView labelView;
  Music song;

  static PagerFragment instance(Music song) {
    PagerFragment fragment = new PagerFragment();
    fragment.song = song;
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.item_coverflow, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  //
  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable("song", (Parcelable) song);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (savedInstanceState != null) {
      // Restore last state for checked position.
      song = savedInstanceState.getParcelable("song");
    }
    Bitmap bitmap = AlbumUtils.parseAlbum(song);
    if (bitmap == null) {
      imageView.setImageResource(R.drawable.default_record_album);
    } else {
      bitmap = AlbumUtils.getCroppedBitmap(bitmap);
      imageView.setImageBitmap(bitmap);
    }
    labelView.setText(song.album());
  }
}
