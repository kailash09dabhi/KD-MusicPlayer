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

public class dEqualizerView extends View {
  Paint paint;
  List<Point> pointList;
  List<Point> curvePivotPointList;
  Point lastTouchedPoint;
  Point screenSize;

  public dEqualizerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public dEqualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    screenSize = new Point();
    pointList = new ArrayList<>();
    curvePivotPointList = new ArrayList<>();
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
        paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
      }
    });
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    for (int i = 0; i < pointList.size(); i++) {
      Point pivotPoint1 = curvePivotPointList.get(i);
      Point pivotPoint2 = curvePivotPointList.get(i + 1);
      Point eqPoint = pointList.get(i);
      int x0 = pivotPoint1.x;
      int y0 = pivotPoint1.y;
      int xc = eqPoint.x;
      int yc = eqPoint.y;
      int x2 = pivotPoint2.x;
      int y2 = pivotPoint2.y;
      int x1 = 2 * xc - x0 / 2 - x2 / 2;
      int y1 = 2 * yc - y0 / 2 - y2 / 2;
      Path path = new Path();

      path.moveTo(x0, y0);
      path.cubicTo(x0, y0, x1, y1, x2, y2);
      canvas.drawPath(path, paint);
      canvas.drawCircle(xc, yc, 5.0f, paint);
      canvas.drawCircle(xc, yc, 10.0f, paint);
    }
    Path path = new Path();
    int x0 = 0;
    int y0 = 200;
    int xc = 100;
    int yc = 100;
    int x2 = 200;
    int y2 = 200;
    int x1 = 2 * xc - x0 / 2 - x2 / 2;
    int y1 = 2 * yc - y0 / 2 - y2 / 2 ;

    path.moveTo(x0, y0);
    path.cubicTo(x0, y0, x1, y1, x2, y2);
    canvas.drawPath(path, paint);
    Path localPath10 = new Path();
    localPath10.moveTo(x0, y0);
    localPath10.addCircle(xc, yc, 1.0F, Path.Direction.CW);
    canvas.drawPath(localPath10, paint);
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
          if (x >= (point.x - 20) && x <= (point.x + 20)) {
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
