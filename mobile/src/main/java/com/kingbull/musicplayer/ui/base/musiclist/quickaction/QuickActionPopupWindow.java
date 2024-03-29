package com.kingbull.musicplayer.ui.base.musiclist.quickaction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.di.AppModule;
import com.kingbull.musicplayer.ui.base.Color;
import com.kingbull.musicplayer.ui.base.theme.ColorTheme;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

public final class QuickActionPopupWindow extends PopupWindows implements OnDismissListener {
  private static final int ANIM_GROW_FROM_LEFT = 1;
  private static final int ANIM_GROW_FROM_RIGHT = 2;
  private static final int ANIM_GROW_FROM_CENTER = 3;
  private static final int ANIM_AUTO = 4;
  //private ImageView mArrowUp;
  //private ImageView mArrowDown;
  private final Animation mTrackAnim;
  private final LayoutInflater inflater;
  private final ViewGroup mTrack;
  private final List<ActionItem> mActionItemList = new ArrayList<>();
  @Inject @Named(AppModule.SMART_THEME) protected ColorTheme smartTheme;
  private OnActionClickListener actionClickListener;
  private OnDismissListener mDismissListener;
  private boolean mDidAction;
  private boolean mAnimateTrack;
  private int mChildPos;
  private int mAnimStyle;

  public QuickActionPopupWindow(Activity context) {
    super(context);
    MusicPlayerApp.instance().component().inject(this);
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mTrackAnim = AnimationUtils.loadAnimation(context, R.anim.rail);
    mTrackAnim.setInterpolator(t -> {
      // Pushes past the target area, then snaps back into place.
      // Equation for graphing: 1.2-((x*1.6)-1.1)^2
      final float inner = (t * 1.55f) - 1.1f;
      return 1.2f - inner * inner;
    });
    rootView = inflater.inflate(R.layout.quickaction, null);
    mTrack = (ViewGroup) rootView.findViewById(R.id.tracks);
    rootView.setLayoutParams(
        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    setContentView(rootView);
    mAnimStyle = ANIM_AUTO;
    mAnimateTrack = true;
    mChildPos = 0;
  }

  /**
   * Animate track.
   *
   * @param mAnimateTrack flag to animate track
   */
  public void mAnimateTrack(boolean mAnimateTrack) {
    this.mAnimateTrack = mAnimateTrack;
  }

  /**
   * Set animation style.
   *
   * @param mAnimStyle animation style, default is set to ANIM_AUTO
   */
  public void setAnimStyle(int mAnimStyle) {
    this.mAnimStyle = mAnimStyle;
  }

  /**
   * Add action item
   *
   * @param action {@link ActionItem}
   */
  public void addActionItem(ActionItem action) {
    mActionItemList.add(action);
    String title = action.title();
    Drawable icon = action.icon();
    View container = inflater.inflate(R.layout.action_item, null);
    ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
    TextView text = (TextView) container.findViewById(R.id.tv_title);
    if (icon != null) {
      img.setImageDrawable(icon);
    } else {
      img.setVisibility(View.GONE);
    }
    if (title != null) {
      text.setText(title);
    } else {
      text.setVisibility(View.GONE);
    }
    final int pos = mChildPos;
    final int actionId = action.actionId();
    container.setOnClickListener(v -> {
      if (actionClickListener != null) {
        actionClickListener.onActionClick(mActionItemList.get(pos));
      }
      if (!getActionItem(pos).isSticky()) {
        mDidAction = true;
        //workaround for transparent background bug
        //thx to Roman Wozniak <roman.wozniak@gmail.com>
        v.post(() -> dismiss());
      }
    });
    container.setFocusable(true);
    container.setClickable(true);
    mTrack.addView(container, mChildPos + 1);
    mChildPos++;
  }

  /**
   * Get action item at an index
   *
   * @param index Index of item (position from callback)
   * @return Action Item at the position
   */
  public ActionItem getActionItem(int index) {
    return mActionItemList.get(index);
  }

  public void addOnActionClickListener(OnActionClickListener listener) {
    actionClickListener = listener;
  }

  /**
   * Show popup popupWindow
   */
  public void show(View anchor) {
    Color color = new Color(smartTheme.quickAction().intValue());
    rootView.findViewById(R.id.scroll).setBackground(color.toDrawable());
    preShow();
    int[] location = new int[2];
    mDidAction = false;
    anchor.getLocationOnScreen(location);
    Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(),
        location[1] + anchor.getHeight());
    //rootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    rootView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    int rootWidth = rootView.getMeasuredWidth();
    int rootHeight = rootView.getMeasuredHeight();
    int screenWidth = windowManager.getDefaultDisplay().getWidth();
    //int screenHeight 	= windowManager.getDefaultDisplay().getHeight();
    int xPos = (screenWidth - rootWidth) / 2;
    int yPos = anchorRect.top - rootHeight;
    boolean onTop = true;
    // display on bottom
    if (rootHeight > anchor.getTop()) {
      yPos = anchorRect.bottom;
      onTop = false;
    }
    //showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), anchorRect.centerX());
    setAnimationStyle(screenWidth, onTop);
    popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    if (mAnimateTrack) mTrack.startAnimation(mTrackAnim);
  }

  /**
   * Set animation style
   *
   * @param screenWidth Screen width
   * @param onTop flag to indicate where the popup should be displayed. Set TRUE if displayed on
   * top
   * of anchor and vice versa
   */
  private void setAnimationStyle(int screenWidth, boolean onTop) {
    int arrowPos = screenWidth;
    switch (mAnimStyle) {
      case ANIM_GROW_FROM_LEFT:
        popupWindow.setAnimationStyle(
            (onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
        break;
      case ANIM_GROW_FROM_RIGHT:
        popupWindow.setAnimationStyle(
            (onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
        break;
      case ANIM_GROW_FROM_CENTER:
        popupWindow.setAnimationStyle(
            (onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
        break;
      case ANIM_AUTO:
        if (arrowPos <= screenWidth / 4) {
          popupWindow.setAnimationStyle(
              (onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
        } else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4)) {
          popupWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
              : R.style.Animations_PopDownMenu_Center);
        } else {
          popupWindow.setAnimationStyle((onTop) ? R.style.Animations_PopDownMenu_Right
              : R.style.Animations_PopDownMenu_Right);
        }
        break;
    }
  }

  /**
   * Set listener for screen dismissed. This listener will only be fired if the quicakction dialog
   * is dismissed
   * by clicking outside the dialog or clicking on sticky item.
   */
  public void setOnDismissListener(QuickActionPopupWindow.OnDismissListener listener) {
    setOnDismissListener(this);
    mDismissListener = listener;
  }

  @Override public void onDismiss() {
    if (!mDidAction && mDismissListener != null) {
      mDismissListener.onDismiss();
    }
  }

  public interface OnActionClickListener {
    void onActionClick(ActionItem actionItem);
  }

  public interface OnDismissListener {
    void onDismiss();
  }
}