package com.kingbull.musicplayer.ui.equalizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.crashlytics.android.Crashlytics;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.domain.EqualizerPreset;
import com.kingbull.musicplayer.domain.storage.sqlite.SqlEqualizerPreset;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/20/2016.
 */
public final class EqualizerView extends View {
  private final Paint paint = new Paint();
  private final Paint linePaint = new Paint();
  private final Paint circlePaint = new Paint();
  private final List<Point> equalizerPointList = new ArrayList<>();
  private final List<Point> curvePivotPointList = new ArrayList<>();
  private final Path curvaturePath = new Path();
  private OnBandValueChangeListener onBandValueChangeListener;
  private RadialGradient radialGradient;
  private Point lastTouchedPoint;
  private int maxHeight;
  private int minHeight;
  private int circleRadius;

  public EqualizerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    circleRadius = getResources().getInteger(R.integer.equalizer_frequency_point_radius);
    setupCurvaturePaint();
    setupLinePaint();
    setupCirclePaint();
    post(() -> {
      maxHeight = getHeight() * 80 / 100;
      minHeight = getHeight() * 20 / 100;
      radialGradient = new RadialGradient(0, getHeight() / 2, getWidth() / 2,
          ContextCompat.getColor(getContext(), R.color.dark_gray_shade),
          ContextCompat.getColor(getContext(), R.color.light_gray_shade), Shader.TileMode.CLAMP);
      paint.setShader(radialGradient);
      linePaint.setColor(new ColorTheme.Flat().header().intValue());
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
    });
  }

  private void setupCurvaturePaint() {
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(getResources().getInteger(R.integer.equalizer_curve_stroke_width));
    paint.setStrokeJoin(Paint.Join.ROUND);
    paint.setStrokeCap(Paint.Cap.ROUND);
  }

  private void setupLinePaint() {
    linePaint.setStyle(Paint.Style.STROKE);
    linePaint.setAntiAlias(true);
    linePaint.setStrokeWidth(
        getResources().getInteger(R.integer.equalizer_frequency_line_stroke_width));
    linePaint.setStrokeJoin(Paint.Join.ROUND);
    linePaint.setStrokeCap(Paint.Cap.ROUND);
  }

  private void setupCirclePaint() {
    circlePaint.setColor(Color.WHITE);
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setAntiAlias(true);
    circlePaint.setStrokeWidth(
        getResources().getInteger(R.integer.equalizer_frequency_point_stroke_width));
  }

  public EqualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
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
        if (lastTouchedPoint != null) {
          if (y <= maxHeight && y >= minHeight) {
            lastTouchedPoint.y = y;
          } else if (y < minHeight) {
            lastTouchedPoint.y = minHeight;
          } else {
            lastTouchedPoint.y = maxHeight;
          }
          invalidate();
        } else {
          Crashlytics.logException(new RuntimeException("how lastTouchedPoint becomes null?",
              new NullPointerException()));
        }
        break;
      case MotionEvent.ACTION_UP:
        if (onBandValueChangeListener != null && lastTouchedPoint != null) {
          onBandValueChangeListener.onBandValueChange(
              (short) equalizerPointList.indexOf(lastTouchedPoint),
              100 - (int) ((lastTouchedPoint.y - minHeight) / ((float) (maxHeight - minHeight))
                  * 100.0));
        } else {
          Crashlytics.logException(new RuntimeException("how lastTouchedPoint becomes null?",
              new NullPointerException()));
        }
        break;
    }
    return true;
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
      //draw straight faded line for denoting the frequency
      canvas.drawLine((xMax - (xMax - xMin) / 2), minHeight, (xMax - (xMax - xMin) / 2), maxHeight,
          linePaint);
      //draw curve now
      curvaturePath.reset();
      curvaturePath.moveTo(xMax, yMax);
      curvaturePath.cubicTo((xMax - (xMax - xMin) / 2) - 10, yMin, (xMax - (xMax - xMin) / 2) + 10,
          yMin, xMin, yMax);
      canvas.drawPath(curvaturePath, paint);
      canvas.drawCircle(xMax - (xMax - xMin) / 2, yDesired, circleRadius, circlePaint);
    }
  }

  private int getMinY(int maxY, int desiredY) {
    return (int) ((4 * desiredY - maxY) / 3.0f);
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

  public EqualizerPreset asEqualizerPreset(String presetName) {
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
            * 100.0), presetName);
  }

  interface OnBandValueChangeListener {
    void onBandValueChange(short bandNumber, int percentageValue);
  }
}