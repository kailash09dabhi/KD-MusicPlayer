package com.kingbull.musicplayer.ui.main.categories.folder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Directory;
import com.kingbull.musicplayer.domain.FileMusicMap;
import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.domain.Milliseconds;
import java.io.File;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class MyFilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final int FOLDER = 0, SONG = 1;

  List<File> files;
  MyFiles.Presenter presenter;
  Directory directory = new Directory();
  @Inject FileMusicMap fileMusicMap;

  public MyFilesAdapter(List<File> files, MyFiles.Presenter presenter) {
    this.files = files;
    this.presenter = presenter;
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    RecyclerView.ViewHolder holder = null;
    switch (viewType) {
      case FOLDER:
        holder = new FolderFileViewHolder(inflater.inflate(R.layout.item_file_song, parent, false));
        break;
      case SONG:
        holder = new SongFileViewHolder(inflater.inflate(R.layout.item_file_song, parent, false));
        break;
    }
    return holder;
  }

  @Override public int getItemViewType(int position) {
    if (files.get(position).isDirectory()) {
      return FOLDER;
    } else {
      return SONG;
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder instanceof FolderFileViewHolder) {
      FolderFileViewHolder holder = (FolderFileViewHolder) viewHolder;
      holder.fileNameView.setText(files.get(position).getName());
      holder.durationView.setText(directory.audioFiles(files.get(position)).size() + " song");
    } else if (viewHolder instanceof SongFileViewHolder) {
      SongFileViewHolder holder = (SongFileViewHolder) viewHolder;
      Media media = fileMusicMap.music(files.get(position)).media();
      holder.fileNameView.setText(media.title());
      holder.artistView.setText(media.artist());
      holder.durationView.setText(new Milliseconds(media.duration()).toMmSs());
    }
  }

  @Override public int getItemCount() {
    return files.size();
  }

  class FolderFileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.fileName) TextView fileNameView;
    @BindView(R.id.durationView) TextView durationView;
    @BindView(R.id.artistView) TextView artistView;

    public FolderFileViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      artistView.setVisibility(View.GONE);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      presenter.onFolderClick(files.get(getAdapterPosition()));
    }
  }

  class SongFileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.fileName) TextView fileNameView;
    @BindView(R.id.durationView) TextView durationView;
    @BindView(R.id.artistView) TextView artistView;

    public SongFileViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      presenter.onMusicClick(files.get(getAdapterPosition()));
    }
  }
}
