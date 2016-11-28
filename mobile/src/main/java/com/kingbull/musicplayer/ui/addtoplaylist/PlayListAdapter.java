package com.kingbull.musicplayer.ui.addtoplaylist;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kingbull.musicplayer.domain.storage.PlayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/28/2016.
 */

public final class PlayListAdapter extends ArrayAdapter<PlayList> {
  LayoutInflater inflater;

  public PlayListAdapter(Context context, List<PlayList> playLists) {
    super(context, 0, playLists);
    inflater = LayoutInflater.from(context);
  }

  @NonNull @Override public View getView(int position, View convertView, ViewGroup parent) {
    TextView textView;
    convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
    textView = (TextView) convertView.findViewById(android.R.id.text1);
    textView.setText(getItem(position).name());
    textView.setTextColor(Color.WHITE);
    return convertView;
  }
}
