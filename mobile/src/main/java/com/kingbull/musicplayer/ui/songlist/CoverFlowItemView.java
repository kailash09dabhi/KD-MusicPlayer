package com.kingbull.musicplayer.ui.songlist;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kingbull.musicplayer.R;

/**
 * @author Kailash Dabhi
 * @date 12/12/2016.
 */

public final class CoverFlowItemView extends LinearLayout {
  View view;
  @BindView(R.id.image) ImageView imageView;
  @BindView(R.id.label) TextView labelView;
  EdgeEffectCompat edgeEffectCompat;

  public CoverFlowItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    view = inflate(getContext(), R.layout.item_coverflow2, this);
    ButterKnife.bind(this, view);
    edgeEffectCompat = new EdgeEffectCompat(getContext());
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    edgeEffectCompat.draw(canvas);
  }
}
