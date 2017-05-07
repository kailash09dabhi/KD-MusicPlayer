package com.kingbull.musicplayer;

import android.content.Context;
import android.preference.PreferenceManager;
import com.github.javiersantos.piracychecker.PiracyChecker;
import com.github.javiersantos.piracychecker.enums.Display;

public final class SmartPiracyGuard implements PiracyGuard {
  private final PiracyChecker checker;

  public SmartPiracyGuard(Context context) {
    this.checker = new PiracyChecker(context).display(Display.ACTIVITY)
        .saveResultToSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context),
            "valid_license")
        .enableUnauthorizedAppsCheck()
        .enableGooglePlayLicensing(
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu9RWitZmhiny3GTqn9N/gYzWV9FfFrmjCX9hbaprBd4+BXJY66tnFkT8o8nlKfE4uI56uABgmOHeol27EWx5l4HSf02LZYdSqhriSCmCFSj5NxClDfSU8r+XlCL0cXiSh0CSs1/gYeBUNPjLkg9L9XSYpXWW6srGMxHxcuEgm2A6tC2BEdpKM0cxDbpJa9ycZsik/Vk5IQHk+z3NG2osM/bk8Y3ZI+zrXIPp99yvmUMNZWF39U+qMB3gNXMlcFJNM3DJGjxKo2hGuFqd7cVkaXEjxPq5NRPnTCO8C65W5eOM3zaqB7mTQhyuGHLnX9frt/loiZfZauqBvrRVxbIYwQIDAQAB");
  }

  @Override public void check() {
    checker.start();
  }

  @Override public void free() {
    checker.destroy();
  }
}