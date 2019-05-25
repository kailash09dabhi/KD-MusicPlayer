package com.kingbull.musicplayer.ui.base.musiclist.edittags;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */

public final class EditTagsDialogFragment extends BaseDialogFragment {
  @BindView(R.id.titleView) EditText titleView;
  @BindView(R.id.artistView) EditText artistView;
  @BindView(R.id.albumView) EditText albumView;
  @BindView(R.id.genreView) EditText genreView;
  @BindView(R.id.yearView) EditText yearView;

  public static EditTagsDialogFragment newInstance(Music music) {
    EditTagsDialogFragment frag = new EditTagsDialogFragment();
    MusicPlayerApp.instance().component().inject(frag);
    Bundle bundle = new Bundle();
    bundle.putParcelable("music", (Parcelable) music);
    frag.setArguments(bundle);
    return frag;
  }

  @OnClick(R.id.cancelButton) void onCancelClick() {
    dismiss();
  }

  @OnClick(R.id.doneButton) void onDoneClick() {
    Music music = getArguments().getParcelable("music");
    try {
      File file = new File(music.media().path());
      MusicTags musicTags = new Mp3agic(file);
      musicTags.edit()
          .album(albumView.getText().toString())
          .artist(artistView.getText().toString())
          .title(titleView.getText().toString())
          .genre(genreView.getText().toString())
          .year(yearView.getText().toString())
          .build()
          .save();
      Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
      intent.setData(Uri.fromFile(file));
      MusicPlayerApp.instance().sendBroadcast(intent);
    } catch (NotSupportedException e) {
      e.printStackTrace();
    } catch (InvalidDataException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (UnsupportedTagException e) {
      e.printStackTrace();
    }
    dismiss();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_edit_tags, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    getDialog().getWindow()
        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    Music music = getArguments().getParcelable("music");
    try {
      MusicTags musicTags = new Mp3agic(new File(music.media().path()));
      titleView.setText(musicTags.title());
      artistView.setText(musicTags.artist());
      albumView.setText(musicTags.album());
      genreView.setText(musicTags.genre());
      yearView.setText(musicTags.year());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (UnsupportedTagException e) {
      e.printStackTrace();
    } catch (InvalidDataException e) {
      e.printStackTrace();
    }
  }
}
