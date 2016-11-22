package com.kingbull.musicplayer.ui.equalizer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/20/2016.
 */

public class CubicEqualizerView extends View {
  Paint paint;
  Paint circlePaint;
  List<Point> pointList;
  List<Point> curvePivotPointList;
  Point lastTouchedPoint;
  Point screenSize;

  public CubicEqualizerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CubicEqualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    screenSize = new Point();
    pointList = new ArrayList<>();
    curvePivotPointList = new ArrayList<>();

    paint = new Paint();
    paint.setColor(Color.DKGRAY);
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(10);

    circlePaint = new Paint();
    circlePaint.setColor(Color.WHITE);
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setAntiAlias(true);
    circlePaint.setStrokeWidth(10);

    post(new Runnable() {
      @Override public void run() {
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getSize(screenSize);
        pointList.add(new Point(screenSize.x / 10 * 1, getHeight() / 2));
        pointList.add(new Point((screenSize.x / 10) * 3, getHeight() / 2));
        pointList.add(new Point((screenSize.x / 10) * 5, getHeight() / 2));
        pointList.add(new Point((screenSize.x / 10) * 7, getHeight() / 2));
        pointList.add(new Point((screenSize.x / 10) * 9, getHeight() / 2));
        curvePivotPointList.add(new Point(0, getHeight() / 2));
        curvePivotPointList.add(new Point(screenSize.x / 10 * 2, getHeight() / 2));
        curvePivotPointList.add(new Point(screenSize.x / 10 * 4, getHeight() / 2));
        curvePivotPointList.add(new Point(screenSize.x / 10 * 6, getHeight() / 2));
        curvePivotPointList.add(new Point(screenSize.x / 10 * 8, getHeight() / 2));
        curvePivotPointList.add(new Point(screenSize.x / 10 * 10, getHeight() / 2));

      }
    });
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    for (int i = 0; i < curvePivotPointList.size() - 1; i++) {
      Point pivotPoint1 = curvePivotPointList.get(i);
      Point pivotPoint2 = curvePivotPointList.get(i + 1);
      Point eqPoint = pointList.get(i);
      Path localPath1 = new Path();
      int yDesired = eqPoint.y;
      int xMin = pivotPoint1.x;
      int yMax = getHeight() / 2;
      int xMax = pivotPoint2.x;
      int yMin = getMinY(yMax, yDesired);
      localPath1.moveTo(xMax, yMax);
      localPath1.cubicTo(xMax, yMin, xMin, yMin, xMin, yMax);
      canvas.drawPath(localPath1, paint);
      canvas.drawCircle(xMax - (xMax - xMin) / 2, yDesired, 10.0f, circlePaint);
    }
  }

  private int getMinY(int maxY, int desiredY) {
    return (int) ((4 * desiredY - maxY) / 3.0f);
  }

  public double cosineInterpolation(double x1, double x2, double normal) {
    double ft = normal * 3.1415927;
    double f = (1 - Math.cos(ft)) * .5;
    return x1 * (1 - f) + x2 * f;
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    int x = (int) event.getX();      //logic to plot the circle in exact touch place
    int y = (int) event.getY();
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        for (Point point : pointList) {
          if (x >= (point.x - screenSize.x / 10) && x <= (point.x + screenSize.x / 10)) {
            lastTouchedPoint = point;
            lastTouchedPoint.y = y;
            invalidate();
            break;
          }
        }
        break;
      case MotionEvent.ACTION_MOVE:
        for (Point point : pointList) {
          if (x >= (point.x - screenSize.x / 10) && x <= (point.x + screenSize.x / 10)) {
            lastTouchedPoint = point;
            lastTouchedPoint.y = y;
            invalidate();
            break;
          }
        }
        break;
    }
    return true;
  }
}