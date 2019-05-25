package com.kingbull.musicplayer.ui.equalizer;

import android.content.Context;
import android.graphics.Matrix;
import androidx.annotation.DrawableRes;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.kingbull.musicplayer.R;

import static com.kingbull.musicplayer.R.drawable.rotoroff;
import static com.kingbull.musicplayer.R.drawable.rotoron;

/**
 * File:              RoundKnobButton
 * Version:           1.0.0
 * Release Date:      November, 2013
 * License:           GPL v2
 * Description:	   A round knob button to control volume and toggle between two states
 *
 * ***************************************************************************
 * Copyright (C) 2013 Radu Motisan  <radu.motisan@gmail.com>
 *
 * http://www.pocketmagic.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * **************************************************************************
 */
public final class RoundKnobButton extends RelativeLayout implements OnGestureListener {
  private final int[] imageRes = {
      R.drawable.stator1, R.drawable.stator2, R.drawable.stator3, R.drawable.stator4,
      R.drawable.stator5, R.drawable.stator6, R.drawable.stator7, R.drawable.stator8,
      R.drawable.stator9, R.drawable.stator10, R.drawable.stator11, R.drawable.stator12,
      R.drawable.stator13, R.drawable.stator14, R.drawable.stator15, R.drawable.stator16,
      R.drawable.stator17, R.drawable.stator18, R.drawable.stator19, R.drawable.stator20,
      R.drawable.stator21
  };
  private final GestureDetector gestureDetector;
  ImageView ivBack;
  private float mAngleDown, mAngleUp;
  private ImageView ivRotor;
  private boolean mState = false;
  private RoundKnobButtonListener m_listener;

  public RoundKnobButton(Context context) {
    super(context);
    // create stator
    ivBack = new ImageView(context);
    ivBack.setImageResource(R.drawable.stator1);
    RelativeLayout.LayoutParams lp_ivBack =
        new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    int innerGapPadding = getResources().getInteger(R.integer.equalizer_knob_inner_gap);
    ivBack.setPadding(innerGapPadding, innerGapPadding, innerGapPadding, innerGapPadding);
    lp_ivBack.addRule(RelativeLayout.CENTER_IN_PARENT);
    addView(ivBack, lp_ivBack);
    // create rotor
    ivRotor = new ImageView(context);
    RelativeLayout.LayoutParams lp_ivKnob =
        new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    //LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    lp_ivKnob.addRule(RelativeLayout.CENTER_IN_PARENT);
    addView(ivRotor, lp_ivKnob);
    // set initial state
    SetState(mState);
    // enable gesture detector
    gestureDetector = new GestureDetector(getContext(), this);
    ivRotor.setImageResource(rotoron);
  }

  public void SetState(boolean state) {
    mState = state;
    ivRotor.setImageResource(state ? rotoron : rotoroff);
  }

  public void addRotationListener(RoundKnobButtonListener l) {
    m_listener = l;
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (gestureDetector.onTouchEvent(event)) {
      return true;
    } else {
      return super.onTouchEvent(event);
    }
  }

  public void setBackgroundResource(@DrawableRes int resId) {
    ivBack.setImageResource(resId);
  }

  public boolean onDown(MotionEvent event) {
    float x = event.getX() / ((float) getWidth());
    float y = event.getY() / ((float) getHeight());
    mAngleDown = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
    return true;
  }

  private float cartesianToPolar(float x, float y) {
    return (float) -Math.toDegrees(Math.atan2(x - 0.5f, y - 0.5f));
  }

  public void onShowPress(MotionEvent e) {
  }

  public boolean onSingleTapUp(MotionEvent e) {
    float x = e.getX() / ((float) getWidth());
    float y = e.getY() / ((float) getHeight());
    mAngleUp = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
    // if we click up the same place where we clicked down, it's just a button press
    if (!Float.isNaN(mAngleDown)
        && !Float.isNaN(mAngleUp)
        && Math.abs(mAngleUp - mAngleDown) < 10) {
      SetState(!mState);
      if (m_listener != null) m_listener.onStateChange(mState);
    }
    return true;
  }

  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    float x = e2.getX() / ((float) getWidth());
    float y = e2.getY() / ((float) getHeight());
    float rotDegrees = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
    if (!Float.isNaN(rotDegrees)) {
      // instead of getting 0-> 180, -180 0 , we go for 0 -> 360
      float posDegrees = rotDegrees;
      if (rotDegrees < 0) posDegrees = 360 + rotDegrees;
      // deny full rotation, start start and stop point, and get a linear scale
      if (posDegrees > 210 || posDegrees < 150) {
        // rotate our imageview
        setRotorPosAngle(posDegrees);
        // get a linear scale
        float scaleDegrees = rotDegrees + 150; // given the current parameters, we go from 0 to 300
        // get position percent
        int percent = (int) (scaleDegrees / 3);
        if (m_listener != null) m_listener.onRotate(percent);
        Log.e("percentage", percent + "---remainder " + (percent / 5));
        ivBack.setImageResource(imageRes[percent / 5]);
        return true; //consumed
      } else {
        return false;
      }
    } else {
      return false; // not consumed
    }
  }

  public void onLongPress(MotionEvent e) {
  }

  public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
    return false;
  }

  public void setRotorPosAngle(float deg) {
    if (deg >= 210 || deg <= 150) {
      if (deg > 180) deg = deg - 360;
      Matrix matrix = new Matrix();
      matrix.postRotate(deg, getWidth() / 2, getHeight() / 2);
      ivRotor.animate().rotation(deg).setDuration(0).start();
    }
  }

  public void setRotorPercentage(int percentage) {
    int posDegree = percentage * 3 - 150;
    if (posDegree < 0) posDegree = 360 + posDegree;
    setRotorPosAngle(posDegree);
    ivBack.setImageResource(imageRes[percentage / 5]);
  }

  interface RoundKnobButtonListener {
    void onStateChange(boolean newstate);

    void onRotate(int percentage);
  }
}