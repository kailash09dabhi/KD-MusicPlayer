package com.kingbull.musicplayer.ui.base.ads;

import android.app.Activity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * @author Kailash Dabhi
 * @date 12/9/2016.
 */
public final class AdmobInterstitial {
  private final InterstitialAd interstitialAd;

  public AdmobInterstitial(Activity activity, String interstitialUnitId,
      final AdListener adListener) {
    interstitialAd = new InterstitialAd(activity);
    interstitialAd.setAdUnitId(interstitialUnitId);
    interstitialAd.setAdListener(new com.google.android.gms.ads.AdListener() {
      @Override public void onAdClosed() {
        adListener.onAdClosed();
      }
    });
  }

  public void load() {
    //if (BuildConfig.DEBUG) return;
    AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .addTestDevice("7CCFDE0BCA5D3295A5406D318F0BBCC1")//LS-5005
        .build();
    interstitialAd.loadAd(adRequest);
  }

  public void showIfLoaded() {
    if (interstitialAd.isLoaded()) interstitialAd.show();
  }

  public interface AdListener {
    void onAdClosed();
  }
}

