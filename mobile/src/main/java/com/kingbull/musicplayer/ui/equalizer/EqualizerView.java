package com.kingbull.musicplayer.ui.equalizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.domain.storage.SqlEqualizerPreset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/20/2016.
 */

public final class EqualizerView extends View {
  private final Paint paint = new Paint();
  private final Paint circlePaint = new Paint();
  private final List<Point> equalizerPointList = new ArrayList<>();
  private final List<Point> curvePivotPointList = new ArrayList<>();
  OnBandValueChangeListener onBandValueChangeListener;
  private RadialGradient radialGradient;
  private Point lastTouchedPoint;
  private int maxHeight;
  private int minHeight;

  public EqualizerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public EqualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(10);
    paint.setStrokeJoin(Paint.Join.ROUND);
    paint.setStrokeCap(Paint.Cap.ROUND);
    circlePaint.setColor(Color.WHITE);
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setAntiAlias(true);
    circlePaint.setStrokeWidth(10);
    post(new Runnable() {
      @Override public void run() {
        maxHeight = getHeight() * 80 / 100;
        minHeight = getHeight() * 20 / 100;
        radialGradient = new RadialGradient(0, getHeight() / 2, getWidth() / 2,
            ContextCompat.getColor(getContext(), R.color.dark_gray_shade),
            ContextCompat.getColor(getContext(), R.color.light_gray_shade), Shader.TileMode.CLAMP);
        paint.setShader(radialGradient);
        equalizerPointList.add(new Point(getWidth() / 10 * 1, getHeight() / 2));
        equalizerPointList.add(new Point((getWidth() / 10) * 3, getHeight() / 2));
        equalizerPointList.add(new Point((getWidth() / 10) * 5, getHeight() / 2));
        equalizerPointList.add(new Point((getWidth() / 10) * 7, getHeight() / 2));
        equalizerPointList.add(new Point((getWidth() / 10) * 9, getHeight() / 2));
        curvePivotPointList.add(new Point(0, getHeight() / 2));
        curvePivotPointList.add(new Point(getWidth() / 10 * 2, getHeight() / 2));
        curvePivotPointList.add(new Point(getWidth() / 10 * 4, getHeight() / 2));
        curvePivotPointList.add(new Point(getWidth() / 10 * 6, getHeight() / 2));
        curvePivotPointList.add(new Point(getWidth() / 10 * 8, getHeight() / 2));
        curvePivotPointList.add(new Point(getWidth() / 10 * 10, getHeight() / 2));
      }
    });
  }

  @Override protected void onDraw(Canvas canvas) {
    for (int i = 0; i < curvePivotPointList.size() - 1; i++) {
      Point pivotPoint1 = curvePivotPointList.get(i);
      Point pivotPoint2 = curvePivotPointList.get(i + 1);
      Point equalizerPoint = equalizerPointList.get(i);
      int yDesired = equalizerPoint.y;
      int xMin = pivotPoint1.x;
      int yMax = getHeight() / 2;
      int xMax = pivotPoint2.x;
      int yMin = getMinY(yMax, yDesired);
      Path localPath1 = new Path();
      localPath1.moveTo(xMax, yMax);
      localPath1.cubicTo((xMax - (xMax - xMin) / 2) - 10, yMin, (xMax - (xMax - xMin) / 2) + 10,
          yMin, xMin, yMax);
      canvas.drawPath(localPath1, paint);
      canvas.drawCircle(xMax - (xMax - xMin) / 2, yDesired, 10.0f, circlePaint);
    }
  }

  private int getMinY(int maxY, int desiredY) {
    return (int) ((4 * desiredY - maxY) / 3.0f);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    int x = (int) event.getX();      //logic to plot the circle in exact touch place
    int y = (int) event.getY();
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        for (Point point : equalizerPointList) {
          if (x >= (point.x - getWidth() / 10) && x <= (point.x + getWidth() / 10)) {
            lastTouchedPoint = point;
            if (y <= maxHeight && y >= minHeight) {
              lastTouchedPoint.y = y;
            } else if (y < minHeight) {
              lastTouchedPoint.y = minHeight;
            } else {
              lastTouchedPoint.y = maxHeight;
            }
            invalidate();
            break;
          }
        }
        break;
      case MotionEvent.ACTION_MOVE:
        if (y <= maxHeight && y >= minHeight) {
          lastTouchedPoint.y = y;
        } else if (y < minHeight) {
          lastTouchedPoint.y = minHeight;
        } else {
          lastTouchedPoint.y = maxHeight;
        }
        invalidate();
        break;
      case MotionEvent.ACTION_UP:
        if (onBandValueChangeListener != null) {
          onBandValueChangeListener.onBandValueChange(
              (short) equalizerPointList.indexOf(lastTouchedPoint),
              100 - (int) ((lastTouchedPoint.y - minHeight) / ((float) (maxHeight - minHeight))
                  * 100.0));
        }
        break;
    }
    return true;
  }

  public void addOnBandValueChangeListener(OnBandValueChangeListener onBandValueChangeListener) {
    this.onBandValueChangeListener = onBandValueChangeListener;
  }

  public void adjustToSelectedPreset(EqualizerPreset equalizerPreset) {
    equalizerPointList.get(0).y = yByPercentage(equalizerPreset.y1Percentage());
    equalizerPointList.get(1).y = yByPercentage(equalizerPreset.y2Percentage());
    equalizerPointList.get(2).y = yByPercentage(equalizerPreset.y3Percentage());
    equalizerPointList.get(3).y = yByPercentage(equalizerPreset.y4Percentage());
    equalizerPointList.get(4).y = yByPercentage(equalizerPreset.y5Percentage());
    invalidate();
  }

  private int yByPercentage(int percentage) {
    return (int) ((100 - percentage) * (maxHeight - minHeight) / 100.0 + minHeight);
  }

  public EqualizerPreset asEqualizerPreset() {
    return new SqlEqualizerPreset(
        100 - (int) ((equalizerPointList.get(0).y - minHeight) / ((float) (maxHeight - minHeight))
            * 100.0),
        100 - (int) ((equalizerPointList.get(1).y - minHeight) / ((float) (maxHeight - minHeight))
            * 100.0),
        100 - (int) ((equalizerPointList.get(2).y - minHeight) / ((float) (maxHeight - minHeight))
            * 100.0),
        100 - (int) ((equalizerPointList.get(3).y - minHeight) / ((float) (maxHeight - minHeight))
            * 100.0),
        100 - (int) ((equalizerPointList.get(4).y - minHeight) / ((float) (maxHeight - minHeight))
            * 100.0),
        "temp");
  }

  interface OnBandValueChangeListener {
    void onBandValueChange(short bandNumber, int percentageValue);
  }
}