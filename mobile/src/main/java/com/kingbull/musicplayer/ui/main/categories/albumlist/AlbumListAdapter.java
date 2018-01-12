package com.kingbull.musicplayer.ui.main.categories.albumlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Album;
import com.kingbull.musicplayer.image.GlideApp;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * @author Kailash Dabhi
 * @date 11/8/2016.
 */
public final class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder>
    implements FastScrollRecyclerView.SectionedAdapter {
  private final List<Album> albumItems;
  private final AlbumList.Presenter presenter;

  public AlbumListAdapter(List<Album> albumItems, AlbumList.Presenter presenter) {
    this.albumItems = albumItems;
    this.presenter = presenter;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Album album = albumItems.get(position);
    holder.labelView.setText(album.name());
    File file = null;
    if (!TextUtils.isEmpty(album.albumArt())) file = new File(album.albumArt());
    GlideApp.with(holder.itemView.getContext())
        .load(album.albumArt())
        .signature(
            new ObjectKey(file == null ? "" : (file.length() + "@" + file.lastModified())))
        .placeholder(R.drawable.bass_guitar)
        .error(R.drawable.ic_music_note)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(holder.imageView);
  }

  @Override public int getItemCount() {
    return albumItems.size();
  }

  @NonNull @Override public String getSectionName(int position) {
    return String.valueOf(
        albumItems.get(position).name().substring(0, 1).toUpperCase(Locale.ENGLISH));
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.label) TextView labelView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      presenter.onAlbumClick(albumItems.get(getAdapterPosition()));
    }
  }
}
