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
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Song;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/12/2016.
 */

public final class MyFilesFragment extends BaseFragment<MyFiles.Presenter> implements MyFiles.View {
  MyFiles.Presenter presenter;
  @BindView(R.id.directoryPathView) TextView directoryPathView;
  @BindView(R.id.recyclerView) RecyclerView recyclerView;

  MyFilesAdapter myFilesAdapter;
  private ArrayList<File> files = new ArrayList<>();

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_my_files, null);
    ButterKnife.bind(this, view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      view.setOnKeyListener(new View.OnKeyListener() {
      @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
          presenter.onBackPressed();
          return true;
        } else {
          return false;
        }
      }
    });
     return view;
  }



  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable("files", files);
  }

  @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      files = (ArrayList<File>) savedInstanceState.get("files");
    }
    super.onViewStateRestored(savedInstanceState);
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

  @Override public void showMusicPlayer(Song song) {
    Intent intent = new Intent(getActivity(), MusicPlayerActivity.class);
    intent.putExtra("song", song);
    startActivity(intent);
    //getActivity().getSupportFragmentManager()
    //    .beginTransaction()
    //    .add(android.R.id.content, MusicPlayerFragment.instance(song))
    //    .addToBackStack(MusicPlayerFragment.class.getSimpleName())
    //    .commit();
  }


  @Override protected void onPresenterPrepared(MyFiles.Presenter presenter) {
    myFilesAdapter = new MyFilesAdapter(files, presenter);
    recyclerView.setAdapter(myFilesAdapter);
    presenter.takeView(this);


  }

  @NonNull @Override protected PresenterFactory presenterFactory() {
    return new PresenterFactory.MyFiles();
  }
}
