package com.kingbull.musicplayer.ui.main;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.kingbull.musicplayer.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class) public class MainActivityTest {

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
