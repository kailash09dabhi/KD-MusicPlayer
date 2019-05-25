package com.kingbull.musicplayer.ui.sorted;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.SortBy;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */
public final class SortDialogFragment extends BaseDialogFragment {
  @BindView(R.id.titleRadioButton) RadioButton titleRadioButton;
  @BindView(R.id.artistRadioButton) RadioButton artistRadioButton;
  @BindView(R.id.albumRadioButton) RadioButton albumRadioButton;
  @BindView(R.id.durationRadioButton) RadioButton durationRadioButton;
  @BindView(R.id.dateAddedRadioButton) RadioButton dateAddedRadioButton;
  @BindView(R.id.yearRadioButton) RadioButton yearRadioButton;
  @BindView(R.id.sortInDescendingCheckbox) CheckBox sortInDescendingCheckbox;

  public static SortDialogFragment newInstance() {
    SortDialogFragment frag = new SortDialogFragment();
    MusicPlayerApp.instance().component().inject(frag);
    return frag;
  }

  @OnClick(R.id.doneButton) void onDoneClick() {
    boolean sortInDescending = sortInDescendingCheckbox.isChecked();
    @SortBy int sortBy = 0;
    if (titleRadioButton.isChecked()) {
      sortBy = SortBy.TITLE;
    } else if (artistRadioButton.isChecked()) {
      sortBy = SortBy.ARTIST;
    } else if (albumRadioButton.isChecked()) {
      sortBy = SortBy.ALBUM;
    } else if (durationRadioButton.isChecked()) {
      sortBy = SortBy.DURATION;
    } else if (dateAddedRadioButton.isChecked()) {
      sortBy = SortBy.DATE_ADDED;
    } else if (yearRadioButton.isChecked()) {
      sortBy = SortBy.YEAR;
    }
    RxBus.getInstance().post(new SortEvent(sortBy, sortInDescending));
    dismiss();
  }

  @OnClick(R.id.closeButton) void onCloseClick() {
    dismiss();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_sort, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
  }
}
