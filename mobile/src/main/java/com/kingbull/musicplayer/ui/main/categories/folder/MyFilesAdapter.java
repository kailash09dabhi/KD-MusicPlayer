package com.kingbull.musicplayer.ui.main.categories.folder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */

public final class MyFilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final int FOLDER = 0, SONG = 1;

  List<File> files;
  MyFiles.Presenter presenter;
  Folder folder = new Folder();

  public MyFilesAdapter(List<File> files, MyFiles.Presenter presenter) {
    this.files = files;
    this.presenter = presenter;
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
      holder.durationView.setText(folder.audioFiles(files.get(position)).size() + " song");
    } else if (viewHolder instanceof SongFileViewHolder) {
      SongFileViewHolder holder = (SongFileViewHolder) viewHolder;
      try {
        Music song = new SongFile(files.get(position)).song(holder.fileNameView.getContext());
        holder.fileNameView.setText(song.title());
        holder.artistView.setText(song.artist());
        holder.durationView.setText(new Milliseconds(song.duration()).toMmSs());
      } catch (IOException e) {
      }
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
      presenter.onEitherFolderOrSongClick(files.get(getAdapterPosition()));
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
      try {
        presenter.onSongClick(
            new SongFile(files.get(getAdapterPosition())).song(view.getContext()));
      } catch (IOException e) {
      }
    }
  }
}
