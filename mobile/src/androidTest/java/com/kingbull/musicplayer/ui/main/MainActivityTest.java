package com.kingbull.musicplayer.ui.main;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.kingbull.musicplayer.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest @RunWith(AndroidJUnit4.class) public class MainActivityTest {

  @Rule public ActivityTestRule<MainActivity> mActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);

  @Test public void mainActivityTest() {
    ViewInteraction recyclerView = onView(allOf(withId(R.id.recyclerView), isDisplayed()));
    recyclerView.perform(actionOnItemAtPosition(0, click()));
    ViewInteraction recyclerView2 = onView(allOf(withId(R.id.recyclerView), isDisplayed()));
    recyclerView2.perform(actionOnItemAtPosition(0, click()));
    ViewInteraction appCompatImageView = onView(allOf(withId(R.id.equalizerView), isDisplayed()));
    appCompatImageView.perform(click());
  }
}
