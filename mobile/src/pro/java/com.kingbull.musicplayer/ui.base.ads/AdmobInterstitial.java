package com.kingbull.musicplayer.ui.base.ads;

import android.app.Activity;

/**
 * @author Kailash Dabhi
 * @date 12/9/2016.
 */
public final class AdmobInterstitial {
  public AdmobInterstitial(Activity activity, String interstitialUnitId, AdListener adListener) {
  }

  public void load() {
  }

  public void showIfLoaded() {
  }
  public interface AdListener {
    void onAdClosed();
  }
}

