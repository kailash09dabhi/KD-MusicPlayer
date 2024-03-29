package com.kingbull.musicplayer.ui.main.categories.folder;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.event.TransparencyChangedEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.ads.AdmobBannerLoaded;
import com.kingbull.musicplayer.ui.base.animators.Alpha;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */
public final class MyFilesFragment extends BaseFragment<MyFiles.Presenter> implements MyFiles.View {
  @BindView(R.id.directoryPathView) TextView directoryPathView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;
  @BindView(R.id.progressBar) ProgressBar progressBar;
  private MyFilesAdapter myFilesAdapter;
  private ArrayList<File> files = new ArrayList<>();
  private final Alpha.Animation animation = new Alpha.Animation();

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_my_files, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    MusicPlayerApp.instance().component().inject(this);
    ButterKnife.bind(this, view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    applyUiColors();
    new AdmobBannerLoaded((ViewGroup) view);
  }

  private void applyUiColors() {
    recyclerView.setBackgroundColor(smartTheme.screen().intValue());
    ((View) directoryPathView.getParent()).setBackgroundColor(smartTheme.tab().intValue());
    directoryPathView.setTextColor(smartTheme.bodyText().intValue());
  }

  @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      files = (ArrayList<File>) savedInstanceState.get("files");
    }
    super.onViewStateRestored(savedInstanceState);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable("files", files);
  }

  @Override public void showFiles(List<File> songs) {
    animation.fadeIn(recyclerView);
    animation.fadeOut(progressBar);
    files.clear();
    files.addAll(songs);
    myFilesAdapter.notifyDataSetChanged();
  }

  @Override public void updateFolder(File file) {
    directoryPathView.setText(file.getAbsolutePath());
  }

  @Override public void close() {
    getActivity().onBackPressed();
  }

  @Override public void showMusicPlayer() {
    Intent intent = new Intent(getActivity(), MusicPlayerActivity.class);
    startActivity(intent);
  }

  @Override public void refresh() {
    myFilesAdapter.notifyDataSetChanged();
    applyUiColors();
  }

  @Override public void showProgressOnFolder(Pair<File, Integer> pairOfFolderAndItsIndex) {
    myFilesAdapter.showProgressOnFolder(pairOfFolderAndItsIndex.second);
  }

  @Override public void hideProgressOnFolder(Pair<File, Integer> pairOfFolderAndItsIndex) {
    myFilesAdapter.hideProgressOnFolder(pairOfFolderAndItsIndex.second);
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                o -> {
                  if (o instanceof PaletteEvent || o instanceof ThemeEvent || o instanceof
                      TransparencyChangedEvent) {
                    if (presenter != null && presenter.hasView()) {
                      presenter.onPaletteOrThemeEvent();
                    } else {
                      FirebaseCrashlytics.getInstance().recordException(
                          new NullPointerException(
                              String.format(
                                  "class: %s presenter- %s hasView- %b",
                                  MyFilesFragment.class.getSimpleName(),
                                  presenter, presenter != null && presenter.hasView()
                              )
                          )
                      );
                    }
                  }
                }
        );
  }

  @Override protected PresenterFactory<MyFiles.Presenter> presenterFactory() {
    return new PresenterFactory.MyFiles();
  }

  @Override protected void onPresenterPrepared(final MyFiles.Presenter presenter) {
    myFilesAdapter = new MyFilesAdapter(files, presenter);
    recyclerView.setAdapter(myFilesAdapter);
    presenter.takeView(this);
    getView().setOnKeyListener((v, keyCode, event) -> {
      if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
        presenter.onBackPressed();
        return true;
      } else {
        return false;
      }
    });
  }
}
