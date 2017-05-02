package com.kingbull.musicplayer.ui.base.analytics;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Kailash Dabhi
 * @date 3/26/2017
 */
public final class FbKeyHash {
  public void print(Context context) {
    try {
      PackageInfo info = context.getPackageManager()
          .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
      for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
      }
    } catch (PackageManager.NameNotFoundException e) {
    } catch (NoSuchAlgorithmException e) {
    }
  }
}
