package com.kingbull.musicplayer.ui.sorted;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
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

  @OnClick(R.id.doneButton) void onDoneClick() {
    boolean sortInDescending = sortInDescendingCheckbox.isChecked();
    @SortEvent.SortBy int sortBy = 0;
    if (titleRadioButton.isChecked()) {
      sortBy = SortEvent.SortBy.TITLE;
    } else if (artistRadioButton.isChecked()) {
      sortBy = SortEvent.SortBy.ARTIST;
    } else if (albumRadioButton.isChecked()) {
      sortBy = SortEvent.SortBy.ALBUM;
    } else if (durationRadioButton.isChecked()) {
      sortBy = SortEvent.SortBy.DURATION;
    } else if (dateAddedRadioButton.isChecked()) {
      sortBy = SortEvent.SortBy.DATE_ADDED;
    } else if (yearRadioButton.isChecked()) {
      sortBy = SortEvent.SortBy.YEAR;
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
