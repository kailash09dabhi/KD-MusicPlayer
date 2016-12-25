package com.kingbull.musicplayer.ui.coverarts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 12/25/2016.
 */

public final class CoverArtsFragment extends BaseFragment<CoverArts.Presenter>
    implements CoverArts.View {
  @BindView(R.id.searchView) EditText searchView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.progress_overlay) ProgressOverlayLayout progressOverlay;
  @BindView(R.id.noResultFound) LinearLayout noResultFoundView;
  private CoverArtsAdapter coverArtsAdapter;
  private List<String> coverArtUrls = new ArrayList<>();
  private Album album;

  public static final CoverArtsFragment newInstanceOfAlbumCovers(Album album) {
    CoverArtsFragment coverArtsFragment = new CoverArtsFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean("isAlbum", true);
    bundle.putParcelable("album", (Parcelable) album);
    coverArtsFragment.setArguments(bundle);
    return coverArtsFragment;
  }

  public static final CoverArtsFragment newInstanceOfArtistCovers(String artistName) {
    CoverArtsFragment coverArtsFragment = new CoverArtsFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean("isAlbum", false);
    bundle.putString("artist_name", artistName);
    coverArtsFragment.setArguments(bundle);
    return coverArtsFragment;
  }

  @OnClick(R.id.searchImage) void onSearchImageClick() {
    if (!TextUtils.isEmpty(searchView.getText())) {
      Bundle bundle = getArguments();
      if (bundle.getBoolean("isAlbum")) {
        presenter.onAlbumSearch(searchView.getText().toString());
      } else {
        presenter.onArtistSearch(searchView.getText().toString());
      }
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_coverarts, null);
    ButterKnife.bind(this, view);
    setupView(view);
    return view;
  }

  private void setupView(View v) {
    recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    coverArtsAdapter = new CoverArtsAdapter(coverArtUrls);
    recyclerView.setAdapter(coverArtsAdapter);
    showProgress();
    Bundle bundle = getArguments();
    album = bundle.getParcelable("album");
    if (bundle.getBoolean("isAlbum")) {
      searchView.setHint("Album Name");
      searchView.setText(album.name());
    } else {
      searchView.setHint("Artist Name");
      searchView.setText(bundle.getString("artist_name", ""));
    }
  }

  @Override protected void onPresenterPrepared(CoverArts.Presenter presenter) {
    presenter.takeView(this);
    coverArtsAdapter.takePresenter(presenter);
    Bundle bundle = getArguments();
    if (bundle.getBoolean("isAlbum")) {
      presenter.onAlbumSearch(album.name());
    } else {
      presenter.onArtistSearch(bundle.getString("artist_name", ""));
    }
  }

  @Override protected PresenterFactory<CoverArts.Presenter> presenterFactory() {
    return new PresenterFactory.CoverArt();
  }

  @Override public void showCoverArts(List<String> imageUrls) {
    int size = coverArtUrls.size();
    coverArtUrls.clear();
    coverArtsAdapter.notifyItemRangeRemoved(0, size);
    coverArtUrls.addAll(imageUrls);
    coverArtsAdapter.notifyItemRangeInserted(0, imageUrls.size());
  }

  @Override public void dismissProgress() {
    progressOverlay.hideProgress();
    recyclerView.setVisibility(View.VISIBLE);
    recyclerView.animate().setDuration(200).alpha(1).setListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        recyclerView.setVisibility(View.VISIBLE);
      }
    });
  }

  @Override public void showProgress() {
    noResultFoundView.setVisibility(View.GONE);
    progressOverlay.showProgress();
    recyclerView.animate().setDuration(200).alpha(0).setListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        recyclerView.setVisibility(View.GONE);
      }
    });
  }

  @Override public void showNoResultFoundMessage() {
    noResultFoundView.setVisibility(View.VISIBLE);
  }

  @Override public void saveCoverArt(String coverArtUrl) {
    final File file = new File(getCoverArtStorageDir("Cover Art"), album.name()+".jpg");
    Glide.with(this).load(coverArtUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
        try {
          FileOutputStream out  = new FileOutputStream(file);
          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
          out.flush();
          out.close();
          album.saveCoverArt(file.getPath());
          new Snackbar(recyclerView).show("Cover art saved successfully!");
          getFragmentManager().popBackStack();
        } catch (FileNotFoundException e) {
          new Snackbar(recyclerView).show("Sorry but cover art not saved:(");
        } catch (IOException e) {
          new Snackbar(recyclerView).show("Sorry but cover art not saved:(");
        }
      }
    });
  }

  private File getCoverArtStorageDir(String folderName) {
    // Get the directory for the user's public pictures directory.
    File file = new File(Environment.getExternalStorageDirectory(),
        MusicPlayerApp.instance().getString(R.string.app_name));
    if (!file.mkdirs()) {
      // Log.e("SignaturePad", "Directory not created");
    }
    file = new File(file, folderName);
    if (!file.mkdirs()) {
      // Log.e("SignaturePad", "Directory not created");
    }
    return file;
  }
}
