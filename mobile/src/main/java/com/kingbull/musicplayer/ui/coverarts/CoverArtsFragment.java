package com.kingbull.musicplayer.ui.coverarts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
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
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
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

  public static final CoverArtsFragment newInstanceOfAlbumCovers(String albumName) {
    CoverArtsFragment coverArtsFragment = new CoverArtsFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean("isAlbum", true);
    bundle.putString("album_name", albumName);
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
    if (bundle.getBoolean("isAlbum")) {
      searchView.setHint("Album Name");
      searchView.setText(bundle.getString("album_name", ""));
    } else {
      searchView.setHint("Artist Name");
      searchView.setText(bundle.getString("artist_name", ""));
    }
  }

  @Override protected void onPresenterPrepared(CoverArts.Presenter presenter) {
    presenter.takeView(this);
    Bundle bundle = getArguments();
    if (bundle.getBoolean("isAlbum")) {
      presenter.onAlbumSearch(bundle.getString("album_name", ""));
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
}
