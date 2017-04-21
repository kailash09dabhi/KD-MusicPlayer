package com.kingbull.musicplayer.ui.addtoplaylist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.PlayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/28/2016.
 */
public final class PlayListAdapter extends ArrayAdapter<PlayList> {
  private LayoutInflater inflater;

  public PlayListAdapter(Context context, List<PlayList> playLists) {
    super(context, 0, playLists);
    inflater = LayoutInflater.from(context);
  }

  @NonNull @Override public View getView(int position, View convertView, ViewGroup parent) {
    TextView textView;
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.item_now_playling, parent, false);
    }
    textView = (TextView) convertView.findViewById(R.id.nameView);
    textView.setText(getItem(position).name());
    return convertView;
  }
}
