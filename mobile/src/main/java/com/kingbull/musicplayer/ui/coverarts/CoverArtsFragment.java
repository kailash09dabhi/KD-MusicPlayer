package com.kingbull.musicplayer.ui.coverarts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.di.StorageModule;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.domain.storage.ImageFile;
import com.kingbull.musicplayer.domain.storage.StorageDirectory;
import com.kingbull.musicplayer.event.CoverArtDownloadedEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 12/25/2016.
 */
public final class CoverArtsFragment extends BaseFragment<CoverArts.Presenter>
    implements CoverArts.View {

  private final StorageDirectory coverArtDir = new StorageDirectory(StorageModule.COVER_ART_DIR);
  private final List<String> coverArtUrls = new ArrayList<>();
  @BindView(R.id.titleView) TextView titleView;
  @BindView(R.id.searchView) EditText searchView;
  @BindView(R.id.searchLayout) LinearLayout searchLayout;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.progress_overlay) ProgressOverlayLayout progressOverlay;
  @BindView(R.id.noResultFound) LinearLayout noResultFoundView;
  private CoverArtsAdapter coverArtsAdapter;
  private Album album;

  public static CoverArtsFragment newInstanceOfAlbumCovers(Album album) {
    CoverArtsFragment coverArtsFragment = new CoverArtsFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean("isAlbum", true);
    bundle.putParcelable("album", (Parcelable) album);
    coverArtsFragment.setArguments(bundle);
    return coverArtsFragment;
  }

  public static CoverArtsFragment newInstanceOfArtistCovers(String artistName) {
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
    View view = inflater.inflate(R.layout.fragment_coverarts, container, false);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    setupView(view);
    return view;
  }

  private void setupView(View v) {
    initializeWithThemeColors(v);
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

  private void initializeWithThemeColors(View v) {
    com.kingbull.musicplayer.ui.base.Color color =
        new com.kingbull.musicplayer.ui.base.Color(flatTheme.screen().intValue());
    titleView.setBackground(color.light().toDrawable());
    v.setBackground(color.toDrawable());
    searchLayout.setBackground(color.light().toDrawable());
    recyclerView.setBackground(color.toDrawable());
  }

  @Override protected PresenterFactory<CoverArts.Presenter> presenterFactory() {
    return new PresenterFactory.CoverArt();
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
    final File file = new File(coverArtDir.asFile(), album.name() + ".jpg");
    Glide.with(this).asBitmap().load(coverArtUrl).into(new SimpleTarget<Bitmap>() {
      @Override public void onResourceReady(@NonNull Bitmap bitmap,
          @Nullable Transition<? super Bitmap> transition) {
        try {
          new ImageFile(file).save(bitmap);
          album = album.saveCoverArt(file.getPath());
          RxBus.getInstance().post(new CoverArtDownloadedEvent());
          new Snackbar(recyclerView).show("Cover art saved successfully!");
          getFragmentManager().popBackStack();
        } catch (IOException e) {
          new Snackbar(recyclerView).show("Sorry but cover art not saved:(");
        }
      }
    });
  }
}
