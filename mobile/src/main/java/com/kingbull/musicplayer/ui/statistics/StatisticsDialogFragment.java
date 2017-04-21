package com.kingbull.musicplayer.ui.statistics;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.Milliseconds;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.ui.base.BaseDialogFragment;

/**
 * @author Kailash Dabhi
 * @date 1/8/2017.
 */

public final class StatisticsDialogFragment extends BaseDialogFragment implements Statistics.View {
  @BindView(R.id.title) TextView titleView;
  @BindView(R.id.artist) TextView artistView;
  @BindView(R.id.numberOfTimesPlayed) TextView numberOfTimesPlayedView;
  @BindView(R.id.totalListenedTime) TextView totalListenedTimeView;
  @BindView(R.id.duration) TextView durationView;
  @BindView(R.id.size) TextView sizeView;

  public static StatisticsDialogFragment newInstance(Music music) {
    StatisticsDialogFragment frag = new StatisticsDialogFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelable("music", (Parcelable) music);
    frag.setArguments(bundle);
    return frag;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_statistics, container, false);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ButterKnife.bind(this, getView());
    Music music = getArguments().getParcelable("music");
    titleView.setText(String.format("Title :  %s", music.media().title()));
    artistView.setText(String.format("Artist :  %s", music.media().artist()));
    numberOfTimesPlayedView.setText(
        String.format("Number of Times Played :  %d", music.mediaStat().numberOfTimesPlayed()));
    totalListenedTimeView.setText(String.format("Total Listened Time :  %d",
        new Milliseconds(music.mediaStat().totalListenedTime()).toString()));
    durationView.setText(
        String.format("duration : %s", new Milliseconds(music.media().duration()).toString()));
    sizeView.setText(String.format("Size :  %.2f Mb", music.media().size() /
        1024 / 1024.0));
  }
}
