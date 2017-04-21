package com.kingbull.musicplayer.ui.main;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.widget.EdgeEffect;
import java.lang.reflect.Field;

/**
 * @author Kailash Dabhi
 * @date 4/3/2017
 */
final class ViewPagerEdgeEffectHack {
  private final ViewPager pager;

  ViewPagerEdgeEffectHack(ViewPager pager) {
    this.pager = pager;
  }

  public void applyColor(int color) {
    try {
      Class<?> clazz = ViewPager.class;
      for (String name : new String[] {
          "mLeftEdge", "mRightEdge"
      }) {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        Object edge = field.get(pager); // android.support.v4.widget.EdgeEffectCompat
        Field fEdgeEffect = edge.getClass().getDeclaredField("mEdgeEffect");
        fEdgeEffect.setAccessible(true);
        applyEdgeEffectColor((EdgeEffect) fEdgeEffect.get(edge), color);
      }
    } catch (Exception ignored) {
    }
  }

  private void applyEdgeEffectColor(EdgeEffect edgeEffect, int color) {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        edgeEffect.setColor(color);
        return;
      }
      Field edgeField = EdgeEffect.class.getDeclaredField("mEdge");
      Field glowField = EdgeEffect.class.getDeclaredField("mGlow");
      edgeField.setAccessible(true);
      glowField.setAccessible(true);
      Drawable mEdge = (Drawable) edgeField.get(edgeEffect);
      Drawable mGlow = (Drawable) glowField.get(edgeEffect);
      mEdge.setColorFilter(color, PorterDuff.Mode.SRC_IN);
      mGlow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
      mEdge.setCallback(null); // free up any references
      mGlow.setCallback(null); // free up any references
    } catch (Exception ignored) {
    }
  }
}
