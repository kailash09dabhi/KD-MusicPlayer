package com.kingbull.musicplayer.ui.equalizer.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.kingbull.musicplayer.R;
import java.util.ArrayList;
import java.util.List;

public final class EqualizerSpinnerAdapter extends BaseAdapter {
  private List<String> mItems = new ArrayList<>();
  private Activity ctx;

  public EqualizerSpinnerAdapter(Activity ctx, List<String> mItems) {
    this.mItems = mItems;
    this.ctx = ctx;
  }

  @Override public int getCount() {
    return mItems.size();
  }

  @Override public Object getItem(int position) {
    return mItems.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getDropDownView(int position, View view, ViewGroup parent) {
    if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
      view = this.ctx.getLayoutInflater().inflate(R.layout.spinner_item_dropdown, parent, false);
      view.setTag("DROPDOWN");
    }
    TextView textView = (TextView) view.findViewById(android.R.id.text1);
    textView.setText(getTitle(position));
    return view;
  }

  @Override public View getView(int position, View view, ViewGroup parent) {
    if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
      view = this.ctx.getLayoutInflater().inflate(R.layout.
          spinner_item_actionbar, parent, false);
      view.setTag("NON_DROPDOWN");
    }
    TextView textView = (TextView) view.findViewById(android.R.id.text1);
    textView.setText(getTitle(position));
    return view;
  }

  private String getTitle(int position) {
    return position >= 0 && position < mItems.size() ? mItems.get(position) : "";
  }
}