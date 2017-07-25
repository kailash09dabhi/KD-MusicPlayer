package com.kingbull.musicplayer;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import com.github.javiersantos.piracychecker.PiracyChecker;
import com.github.javiersantos.piracychecker.PiracyCheckerUtils;
import com.github.javiersantos.piracychecker.enums.Display;

public final class SmartPiracyGuard implements PiracyGuard {
  private final PiracyChecker checker;
  private final String licenseKeyBase64 =
      "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu9RWitZmhiny3GTqn9N/gYzWV9FfFrmjCX9hbaprBd4+BXJY66tnFkT8o8nlKfE4uI56uABgmOHeol27EWx5l4HSf02LZYdSqhriSCmCFSj5NxClDfSU8r+XlCL0cXiSh0CSs1/gYeBUNPjLkg9L9XSYpXWW6srGMxHxcuEgm2A6tC2BEdpKM0cxDbpJa9ycZsik/Vk5IQHk+z3NG2osM/bk8Y3ZI+zrXIPp99yvmUMNZWF39U+qMB3gNXMlcFJNM3DJGjxKo2hGuFqd7cVkaXEjxPq5NRPnTCO8C65W5eOM3zaqB7mTQhyuGHLnX9frt/loiZfZauqBvrRVxbIYwQIDAQAB";
  private final String productionSignature = "jakdaRXY0s7v1GoQStBtk2cliuI=";
  private final String debugSignature = "l4daumxV6ujKWVULjiffryt3CHQ=";

  public SmartPiracyGuard(Context context) {
    if (BuildConfig.DEBUG) {
      Log.e("SIGNATURE", PiracyCheckerUtils.getAPKSignature(context));
    }
    this.checker = new PiracyChecker(context).display(Display.ACTIVITY)
        .saveResultToSharedPreferences(
            PreferenceManager.getDefaultSharedPreferences(context),
            "valid_license"
        )
        .enableSigningCertificate(productionSignature)
        .enableUnauthorizedAppsCheck()
        .enableGooglePlayLicensing(licenseKeyBase64);
  }

  @Override public void check() {
    checker.start();
  }

  @Override public void free() {
    checker.destroy();
  }
}