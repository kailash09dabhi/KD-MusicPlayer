package com.kingbull.musicplayer.ui.main.categories.folder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.crash.FirebaseCrash;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
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
  private MyFilesAdapter myFilesAdapter;
  private ArrayList<File> files = new ArrayList<>();

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_my_files, container, false);
    ButterKnife.bind(this, view);
    MusicPlayerApp.instance().component().inject(this);
    applyUIColors();
    return view;
  }

  private void applyUIColors() {
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
    applyUIColors();
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
          @Override public void accept(Object o) throws Exception {
            if (o instanceof PaletteEvent || o instanceof ThemeEvent) {
              if (presenter != null) {
                presenter.onPaletteOrThemeEvent();
              } else {
                FirebaseCrash.report(new NullPointerException(
                    "presenter is null! this is weird as MyFilesFragment receiving events means onDestroy() is not called yet, then why should presenter becomes null"));
              }
            }
          }
        });
  }

  @NonNull @Override protected PresenterFactory<MyFiles.Presenter> presenterFactory() {
    return new PresenterFactory.MyFiles();
  }

  @Override protected void onPresenterPrepared(final MyFiles.Presenter presenter) {
    myFilesAdapter = new MyFilesAdapter(files, presenter);
    recyclerView.setAdapter(myFilesAdapter);
    presenter.takeView(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    getView().setOnKeyListener(new View.OnKeyListener() {
      @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
          presenter.onBackPressed();
          return true;
        } else {
          return false;
        }
      }
    });
  }
}
