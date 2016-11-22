package com.kingbull.musicplayer.ui.equalizer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 11/20/2016.
 */

public class EqualizerView extends View {
  Paint paint;
  Paint circlePaint;
  List<Point> pointList;
  List<Point> curvePivotPointList;
  Point lastTouchedPoint;
  Point screenSize;
  RadialGradient radialGradient;
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
    screenSize = new Point();
    pointList = new ArrayList<>();
    curvePivotPointList = new ArrayList<>();
    paint = new Paint();
    //paint.setColor(Color.parseColor("#656360"));
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(10);
    paint.setStrokeJoin(Paint.Join.ROUND);
    paint.setStrokeCap(Paint.Cap.ROUND);
    circlePaint = new Paint();
    circlePaint.setColor(Color.WHITE);
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setAntiAlias(true);
    circlePaint.setStrokeWidth(10);
    post(new Runnable() {
      @Override public void run() {
        maxHeight = getHeight() * 80 / 100;
        minHeight = getHeight() * 20 / 100;
        radialGradient = new RadialGradient(getWidth() / 2, getHeight() / 2, getHeight() * 2 / 5,
            Color.parseColor("#656360"), Color.parseColor("#656360"), Shader.TileMode.CLAMP);
        paint.setShader(radialGradient);
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
    for (int i = 0; i < curvePivotPointList.size() - 1; i++) {
      Point pivotPoint1 = curvePivotPointList.get(i);
      Point pivotPoint2 = curvePivotPointList.get(i + 1);
      Point eqPoint = pointList.get(i);
      int yDesired = eqPoint.y;
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
        for (Point point : pointList) {
          if (x >= (point.x - screenSize.x / 10) && x <= (point.x + screenSize.x / 10)) {
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
    }
    return true;
  }
}