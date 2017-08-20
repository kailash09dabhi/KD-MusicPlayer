package com.kingbull.musicplayer.ui.main.categories.folder;

import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.di.AppModule;
import com.kingbull.musicplayer.domain.FileMusicMap;
import com.kingbull.musicplayer.domain.Media;
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.ui.base.DefaultDisposableObserver;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class MyFilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final int FOLDER = 0, SONG = 1;
  private final List<File> files;
  @Inject @Named(AppModule.SMART_THEME) ColorTheme colorTheme;
  @Inject FileMusicMap fileMusicMap;
  private MyFiles.Presenter presenter;

  public MyFilesAdapter(final List<File> files, MyFiles.Presenter presenter) {
    this.files = files;
    this.presenter = presenter;
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    RecyclerView.ViewHolder holder = null;
    switch (viewType) {
      case FOLDER:
        holder = new FolderFileViewHolder(inflater.inflate(R.layout.item_folder, parent, false));
        break;
      case SONG:
        holder = new SongFileViewHolder(inflater.inflate(R.layout.item_file_song, parent, false));
        break;
    }
    return holder;
  }

  int loadingFolderPosition = -1;

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
    int titleColor = colorTheme.titleText().intValue();
    int bodyColor = colorTheme.bodyText().intValue();
    if (viewHolder instanceof FolderFileViewHolder) {
      final FolderFileViewHolder holder = (FolderFileViewHolder) viewHolder;
      holder.folderNameView.setText(files.get(position).getName());
      holder.totalSongCountView.setText("");
      Observable.fromCallable(new Callable<Integer>() {
        @Override public Integer call() throws Exception {
          return new Folder.Cached(Folder.Smart.from(files.get(position))).allMusics().size();
        }
      }).subscribeOn(Schedulers.io()).map(new Function<Integer, String>() {
        @Override public String apply(@NonNull Integer integer) throws Exception {
          return String.valueOf(integer);
        }
      }).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<String>() {
        @Override public void accept(@NonNull String s) throws Exception {
          holder.totalSongCountView.setText(s + " song");
        }
      }).subscribe(new DefaultDisposableObserver<String>());
      holder.folderNameView.setTextColor(titleColor);
      holder.totalSongCountView.setTextColor(bodyColor);
      if (loadingFolderPosition == position) {
        holder.itemView.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.getContext(), R.color.transparent_strong_black));
        holder.progressBar.setVisibility(View.VISIBLE);
      } else {
        holder.itemView.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.getContext(), R.color.transparent));
        holder.progressBar.setVisibility(View.GONE);

      }
    } else if (viewHolder instanceof SongFileViewHolder) {
      SongFileViewHolder holder = (SongFileViewHolder) viewHolder;
      Media media = fileMusicMap.music(files.get(position)).media();
      holder.fileNameView.setText(media.title());
      holder.artistView.setText(media.artist());
      holder.durationView.setText(new Milliseconds(media.duration()).toMmSs());
      holder.fileNameView.setTextColor(titleColor);
      holder.artistView.setTextColor(bodyColor);
      holder.durationView.setTextColor(bodyColor);
    }
  }

  @Override public int getItemViewType(int position) {
    if (files.get(position).isDirectory()) {
      return FOLDER;
    } else {
      return SONG;
    }
  }

  @Override public int getItemCount() {
    return files.size();
  }

  public void hideProgressOnFolder(int index) {
    loadingFolderPosition = -1;
    notifyItemChanged(index);
  }

  public void showProgressOnFolder(int index) {
    loadingFolderPosition = index;
    notifyItemChanged(index);
  }

  class FolderFileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.folderName) TextView folderNameView;
    @BindView(R.id.totalSongCountView) TextView totalSongCountView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    public FolderFileViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      int adapterPosition = getAdapterPosition();
      if (adapterPosition != RecyclerView.NO_POSITION) {
        presenter.onFolderClick(new Pair<>(files.get(adapterPosition), adapterPosition));
      }
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
      int adapterPosition = getAdapterPosition();
      if (adapterPosition != RecyclerView.NO_POSITION) {
        presenter.onMusicClick(files.get(adapterPosition));
      }
    }
  }
}
