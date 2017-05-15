package com.kingbull.musicplayer.ui.music.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/8/16
 * Time: 4:23 PM
 * Desc: ShadowImageView
 * Stole from {@link android.support.v4.widget.SwipeRefreshLayout}'s implementation to display
 * beautiful ic_shadow
 * for circle ImageView.
 */
public final class ShadowImageView extends android.support.v7.widget.AppCompatImageView {
  private static final int KEY_SHADOW_COLOR = 0x1E000000;
  private static final int FILL_SHADOW_COLOR = 0x3D000000;
  private static final float X_OFFSET = 0f;
  private static final float Y_OFFSET = 1.75f;
  private static final float SHADOW_RADIUS = 24f;
  private static final int SHADOW_ELEVATION = 16;
  private static final int DEFAULT_BACKGROUND_COLOR = 0xFF3C5F78;
  //Disposable disposable;
  private int shadowRadius;
  // Animation
  private ObjectAnimator rotateAnimator;
  private long lastAnimationValue;

  public ShadowImageView(Context context) {
    this(context, null);
  }

  public ShadowImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    final float density = getContext().getResources().getDisplayMetrics().density;
    final int shadowXOffset = (int) (density * X_OFFSET);
    final int shadowYOffset = (int) (density * Y_OFFSET);
    shadowRadius = (int) (density * SHADOW_RADIUS);
    ShapeDrawable circle;
    if (elevationSupported()) {
      circle = new ShapeDrawable(new OvalShape());
      ViewCompat.setElevation(this, SHADOW_ELEVATION * density);
    } else {
      OvalShape oval = new OvalShadow(shadowRadius);
      circle = new ShapeDrawable(oval);
      ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, circle.getPaint());
      circle.getPaint()
          .setShadowLayer(shadowRadius, shadowXOffset, shadowYOffset, KEY_SHADOW_COLOR);
      final int padding = shadowRadius;
      // set padding so the inner image sits correctly within the ic_shadow.
      setPadding(padding, padding, padding, padding);
    }
    circle.getPaint().setAntiAlias(true);
    circle.getPaint().setColor(DEFAULT_BACKGROUND_COLOR);
    setBackground(circle);
    rotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
    rotateAnimator.setDuration(7200);
    rotateAnimator.setInterpolator(new LinearInterpolator());
    rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
    rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
  }

  private boolean elevationSupported() {
    return Build.VERSION.SDK_INT >= 21;
  }
  // Animation

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (!elevationSupported()) {
      setMeasuredDimension(getMeasuredWidth() + shadowRadius * 2,
          getMeasuredHeight() + shadowRadius * 2);
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (rotateAnimator != null) {
      rotateAnimator.cancel();
      rotateAnimator = null;
    }
  }

  public void startRotateAnimation() {
    rotateAnimator.cancel();
    rotateAnimator.start();
  }

  public void pauseRotateAnimation() {
    lastAnimationValue = rotateAnimator.getCurrentPlayTime();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      rotateAnimator.pause();
    } else {
      rotateAnimator.cancel();
      rotateAnimator.end();
    }
    //rotateAnimator.end();
    //if (disposable != null && !disposable.isDisposed()) disposable.dispose();
  }

  public void resumeRotateAnimation() {
    rotateAnimator.setCurrentPlayTime(lastAnimationValue);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      rotateAnimator.resume();
    } else {
      rotateAnimator.start();
    }
    //if (disposable!=null && !disposable.isDisposed()) disposable.dispose();
    //disposable = Observable.interval(16, TimeUnit.MILLISECONDS)
    //    .observeOn(AndroidSchedulers.mainThread())
    //    .subscribe(new Consumer<Long>() {
    //      int rotation = 0;
    //
    //      @Override public void accept(Long aLong) throws Exception {
    //        setRotation(rotation++);
    //        if (rotation > 360) rotation = 0;
    //      }
    //    });
  }

  /**
   * Draw oval ic_shadow below ImageView under lollipop.
   */
  private class OvalShadow extends OvalShape {
    private final Paint shadowPaint;
    private RadialGradient radialGradient;

    OvalShadow(int shadowRadius) {
      super();
      shadowPaint = new Paint();
      ShadowImageView.this.shadowRadius = shadowRadius;
      updateRadialGradient((int) rect().width());
    }

    private void updateRadialGradient(int diameter) {
      radialGradient = new RadialGradient(diameter / 2, diameter / 2, shadowRadius,
          new int[] { FILL_SHADOW_COLOR, Color.TRANSPARENT }, null, Shader.TileMode.CLAMP);
      shadowPaint.setShader(radialGradient);
    }

    @Override protected void onResize(float width, float height) {
      super.onResize(width, height);
      updateRadialGradient((int) width);
    }

    @Override public void draw(Canvas canvas, Paint paint) {
      final int viewWidth = ShadowImageView.this.getWidth();
      final int viewHeight = ShadowImageView.this.getHeight();
      canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2, shadowPaint);
      canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewWidth / 2 - shadowRadius, paint);
    }
  }
}
